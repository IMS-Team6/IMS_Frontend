package com.example.myapplication

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.beust.klaxon.Klaxon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Math.random
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates
import kotlin.random.Random.Default.nextInt


class MapHistoryFragment : Fragment() {
    private lateinit var viewOfLayout: View
    private lateinit var client: OkHttpClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_map_history, container, false)

        // Setup history map.
        val historyGraph: GraphView = viewOfLayout.findViewById(R.id.mapHistoryGraph)
        historyGraph.viewTreeObserver.addOnGlobalLayoutListener(GetMapLocation())

        // Create OkHttp Client.
        client = OkHttpClient()
        Log.d("status", "Fetching sessions...")
        fetch("http://3.72.195.76/api/sessions")

        // Setup dropdown menu
        // Todo: Remove dummy data and populate spinner with actual data fetched from the backend.
        val spinner: Spinner = viewOfLayout.findViewById(R.id.moverHistorySpinner)
        val testArray = resources.getStringArray(R.array.test_array)
        val adapter = activity?.applicationContext?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, testArray) }
        spinner.adapter = adapter

        return viewOfLayout
    }

    private fun fetch(sUrl: String): Sessions? {
        var sessions: Sessions? = null

        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)

            if (result != null) {

                Log.d("RESULT", result)

                /*
                try {
                    // Parse result string JSON to data class
                    sessions = Klaxon().parse<Sessions>(result)

                    withContext(Dispatchers.Main) {
                    Log.d("RESULT", "Success!")
                }

                } catch (error: Error) {
                    Log.d("ERROR", error.toString())
                }
                */
            } else {
                Log.d("ERROR", "Request returned no response!")
            }
        }

        return sessions
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
            var mapRect = Rect(mapView.left, (mapView.top - graphTitle.height), mapView.right, (mapView.bottom - graphTitle.height))

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

data class Sessions(val sessionId: Int, val robotState: String, val isCollision: Boolean)