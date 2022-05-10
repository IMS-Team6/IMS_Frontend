package com.example.myapplication

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
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
    private lateinit var getMapLocation: GetMapLocation
    private var positions = mutableListOf<DataPoint>()
    private var collisions = mutableListOf<DataPoint>()

    private val baseURL: String = "http://3.72.195.76/api/"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_map_history, container, false)

        // Create OkHttp Client.
        client = OkHttpClient()

        // Setup history map.
        getMapLocation = GetMapLocation(mutableListOf(), mutableListOf())

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
            val dataPoint = DataPoint(x, y)
            dataPoints.add(dataPoint)
        }

        return dataPoints
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
                            Log.d("fetchSession", "Converting...")
                            positions = convertToDataPoints(posX, posY)
                            collisions = convertToDataPoints(collX, collY)

                            // TODO: Make these line synchronous!
                            // Change data values on MapLayout
                            Log.d("fetchSession", "Changing...")
                            getMapLocation.moverPositions = positions
                            getMapLocation.moverCollisions = collisions

                            val historyGraph: GraphView = viewOfLayout.findViewById(R.id.mapHistoryGraph)
                            historyGraph.viewTreeObserver.addOnGlobalLayoutListener(getMapLocation)

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

    internal inner class GetMapLocation(positions: MutableList<DataPoint>, collisions: MutableList<DataPoint>) : ViewTreeObserver.OnGlobalLayoutListener {

        var moverPositions: List<DataPoint> = positions
        var moverCollisions: List<DataPoint> = collisions

        init {
            Log.d("MAP", "Init MAP! ${moverPositions.size}")
        }

        override fun onGlobalLayout() {
            Log.d("MAP", "Changing map state...")
            val graphTitle: TextView = viewOfLayout.findViewById((R.id.mapHistoryTitle))
            val mapView: GraphView = viewOfLayout.findViewById(R.id.mapHistoryGraph)

            // Create a rectangle that represent the map boundaries.
            val mapRect = Rect(mapView.left, (mapView.top - graphTitle.height), mapView.right, (mapView.bottom - graphTitle.height))

            val myList = mutableListOf<DataPoint>()

            if (moverPositions.isNotEmpty()) {
                moverPositions.forEach {
                    val adjustX = mapRect.centerX() + it.xVal
                    val adjustY = mapRect.centerY() + it.yVal
                    myList.add(DataPoint(adjustX, adjustY))
                }
            } else {
                Toast.makeText(activity, "Session does not contain any position data.", Toast.LENGTH_SHORT).show()
            }

            mapView.setData(myList)
        }
    }
}
