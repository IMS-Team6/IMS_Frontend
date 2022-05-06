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

    private val BASE_URL: String = "http://3.72.195.76/api/"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_map_history, container, false)

        // Setup history map.
        val historyGraph: GraphView = viewOfLayout.findViewById(R.id.mapHistoryGraph)
        historyGraph.viewTreeObserver.addOnGlobalLayoutListener(GetMapLocation())

        // Create OkHttp Client.
        client = OkHttpClient()

        // Fetch mover sessions from backend API.
        fetchSessions(BASE_URL + "sessions")

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

                fetchSession(BASE_URL + "session/" + selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Spinner", "Nothing!")
            }
        }
    }

    private fun fetchSession(sUrl: String) {
        Log.d("Spinner", "Fetching session...")

        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)

            if (result != null) {
                try {
                    Log.d("fetchSession", result)
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
                    val mapper = Klaxon()
                    val sessions: List<SessionInfo>? = mapper.parseArray(result)

                    withContext(Dispatchers.Main) {
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

    internal inner class GetMapLocation: ViewTreeObserver.OnGlobalLayoutListener {

        override fun onGlobalLayout() {
            val graphTitle: TextView = viewOfLayout.findViewById((R.id.mapHistoryTitle))
            val mapView: GraphView = viewOfLayout.findViewById(R.id.mapHistoryGraph)

            // Create a rectangle that represent the map boundaries.
            val mapRect = Rect(mapView.left, (mapView.top - graphTitle.height), mapView.right, (mapView.bottom - graphTitle.height))

            // Generate random dummy data points to be rendered on map.
            val random = Random()
            val myList = mutableListOf<DataPoint>()

            for (i in 0..10) {
                val x = random.nextInt(300) + mapRect.centerX()
                val y = random.nextInt(500) + mapRect.centerY()

                val randomDataPoint = DataPoint(x, y)
                myList.add(randomDataPoint)
            }

            mapView.setData(myList)
        }
    }

}

data class SessionInfo(val sessionID: String, val robotState: String, val collision: java.lang.Boolean)

