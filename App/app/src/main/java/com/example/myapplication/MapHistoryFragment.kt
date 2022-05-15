package com.example.myapplication

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity.apply
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.core.view.GravityCompat.apply
import androidx.lifecycle.lifecycleScope
import com.beust.klaxon.Klaxon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL
import java.util.*


class MapHistoryFragment : Fragment() {
    private lateinit var viewOfLayout: View
    private lateinit var client: OkHttpClient

    private lateinit var graphTitle: TextView
    private lateinit var mapView: ImageView
    private lateinit var mapCanvas: MapCanvas

    private var xValMax: Int = 0
    private var xValMin: Int = 0
    private var yValMax: Int = 0
    private var yValMin: Int = 0

    private val baseURL: String = "http://3.72.195.76/api/"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_map_history, container, false)

        // Create OkHttp Client.
        client = OkHttpClient()

        // Setup history map.
        graphTitle = viewOfLayout.findViewById((R.id.sessionText))
        mapView = viewOfLayout.findViewById(R.id.mapHistoryGraph)

        // Fetch mover sessions from backend API.
        fetchSessions(baseURL + "sessions")

        return viewOfLayout
    }

    private fun populateSpinnerWithSessions(sessions: List<SessionInfo>) {
        val sessionsIds = arrayListOf<String>()

        for (session in sessions) {
            sessionsIds.add(session.sessionID)
        }

        val spinner: Spinner = viewOfLayout.findViewById(R.id.moverHistorySpinner)
        spinner.adapter = context?.let { ArrayAdapter(it, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sessionsIds) }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                Log.d("SPINNER", "$selectedItem selected!")

                fetchSession(baseURL + "session/" + selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Spinner", "Nothing!")
            }
        }
    }

    private fun stringToList(str: String): List<String> {
        val removeWhiteSpaces = str.replace(" ", "")
        val formatString = removeWhiteSpaces.substring(1, removeWhiteSpaces.length-1).split(",")
        return formatString.toList()
    }

    private fun convertToDataPoints(xValues: String, yValues: String): MutableList<DataPoint> {
        var dataPoints = mutableListOf<DataPoint>()

        if (xValues.isEmpty() || yValues.isEmpty()) {
            return dataPoints
        }

        // Format strings by removing whitespaces, commas and brackets.
        val formatX = stringToList(xValues)
        val formatY = stringToList(yValues)

        if (formatX.size != formatY.size || formatX.size <= 1 || formatY.size <= 1) {
            return dataPoints
        }

        val listSize = formatX.size - 1

        for (i in 0..listSize) {
            val x = formatX[i].toInt()
            val y = formatY[i].toInt()

            // Check if current x and y values are bigger/smaller than the current max and min values for x and y.
            checkXValues(x)
            checkYValues(y)

            val dataPoint = DataPoint(x, y)
            dataPoints.add(dataPoint)
        }

        return dataPoints
    }

    private fun checkXValues(x: Int) {
        if (xValMax < x) {
            xValMax = x
        } else if (x < xValMin) {
            xValMin = x
        }
    }

    private fun checkYValues(y: Int) {
        if (yValMax < y) {
            yValMax = y
        } else if (y < yValMin) {
            yValMin = y
        }
    }

    private fun getScaleConstant(rect: Rect): Int {
        for (n in 50 downTo 2) {
            Log.d("scaleConstant", n.toString())

            // Add min values to a datapoint
            val xMin = rect.centerX() + (xValMin * n)
            val yMin = rect.centerY() + (yValMin * n)

            // Add max values to a datapoint
            val xMax = rect.centerX() + (xValMax * n)
            val yMax = rect.centerY() + (yValMax * n)

            if (rect.contains(xMin, yMin) && rect.contains(xMax, yMax)) {
                return n
            }
        }

        return 1
    }

    private fun drawOnCanvas(positions: MutableList<DataPoint>, collisions: MutableList<DataPoint>) {
        // Create a rectangle that represent the map boundaries.
        val mapRect = Rect(mapView.left, mapView.top, mapView.right, mapView.bottom)

        // Create canvas and bitmap.
        val bitmap = Bitmap.createBitmap(mapRect.width(), mapRect.height(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR) // Reset canvas from previous map drawing

        if (positions.isNotEmpty()) {
            val scaleConstant = getScaleConstant(mapRect)
            Log.d("res", "Result: $scaleConstant")

            val blackDot =
                Paint().apply {
                    isAntiAlias = true
                    color = Color.BLACK
                    style = Paint.Style.FILL
                }

            val blackLine =
                Paint().apply {
                    isAntiAlias = true
                    color = Color.BLACK
                    style = Paint.Style.STROKE
                    strokeWidth = 5f
                }

            val redDot =
                Paint().apply {
                    isAntiAlias = true
                    color = Color.RED
                    style = Paint.Style.FILL
                }

            var previousDataPoint: DataPoint? = null

            for (pos in positions) {
                // Format coordinates so that (0,0) is positioned at center of the map instead of top left corner.
                // Format coordinates to that the x and y values gets scaled to the value of the scale constant.
                val formatX = mapRect.centerX() + (pos.xVal * scaleConstant)
                val formatY = mapRect.centerY() + (pos.yVal * scaleConstant)

                if (previousDataPoint != null) {
                    // Draw line between the current and the previous data point.
                    canvas.drawLine(previousDataPoint.xVal.toFloat(), previousDataPoint.yVal.toFloat(), formatX.toFloat(), formatY.toFloat(), blackLine)
                }
                previousDataPoint = DataPoint(formatX, formatY)

                canvas.drawCircle(formatX.toFloat(), formatY.toFloat(), 10f, blackDot)
            }


            if (collisions.isNotEmpty()) {
                // Draw out collisions on map, if there exists any.
                for (col in collisions) {
                    // Format coordinates so that (0,0) is positioned at center of the map instead of top left corner.
                    // Format coordinates to that the x and y values gets scaled to the value of the scale constant.
                    val formatX = mapRect.centerX() + (col.xVal * scaleConstant)
                    val formatY = mapRect.centerY() + (col.yVal * scaleConstant)
                    canvas.drawCircle(formatX.toFloat(), formatY.toFloat(), 10f, redDot)
                }
            }
        } else {
            Toast.makeText(activity, "No position data to draw on the selected session session", Toast.LENGTH_SHORT).show()
        }

        mapView.background = BitmapDrawable(resources, bitmap)
    }

    private fun fetchSession(sUrl: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)

            if (result != null) {
                try {
                    withContext(Dispatchers.Main) {
                        val mapper = Klaxon()
                        val session = mapper.parse<Session>(result)

                        if (session != null) {
                            // Convert position data to strings
                            val posX = session.positions["posX"].toString()
                            val posY = session.positions["posY"].toString()

                            // Convert collision data to strings
                            val collX = session.collisionPos["collX"].toString()
                            val collY = session.collisionPos["collY"].toString()

                            // Convert collision and position data to list of DataPoints
                            val positions = convertToDataPoints(posX, posY)
                            val collisions = convertToDataPoints(collX, collY)

                            drawOnCanvas(positions, collisions)
                        } else {
                            Log.d("ERROR", "Could not parse session object!")
                        }
                    }
                }
                catch (error: java.lang.Error) {
                    Log.d("ERROR", error.toString())
                    Toast.makeText(activity,error.toString(),Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("ERROR", "Request returned no response!")
                Toast.makeText(activity,"Request returned no response!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun fetchSessions(sUrl: String){
        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)

            if (result != null) {
                try {
                    withContext(Dispatchers.Main) {
                        val mapper = Klaxon()
                        val sessions: List<SessionInfo>? = mapper.parseArray(result)

                        if (sessions != null) {
                            populateSpinnerWithSessions(sessions)
                        } else {
                            Toast.makeText(activity,"List of sessions is empty!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (error: Error) {
                    Log.d("ERROR", error.toString())
                    Toast.makeText(activity,error.toString(),Toast.LENGTH_SHORT).show();
                }

            } else {
                Log.d("ERROR", "Request returned no response!")
                Toast.makeText(activity,"Request returned no response!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun getRequest(sUrl: String): String? {
        var result: String? = null

        try {
            // Create URL
            val url = URL(sUrl)

            // Build request
            val request = Request.Builder().url(url).build()

            // Execute request
            val response = client.newCall(request).execute()
            result = response.body?.string()
        } catch (error: Error) {
            Log.d("ERROR", error.toString())
        }

        return result
    }
}


