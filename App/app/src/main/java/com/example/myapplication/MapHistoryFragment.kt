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
import java.lang.Math.random
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates
import kotlin.random.Random.Default.nextInt


class MapHistoryFragment : Fragment() {
    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_map_history, container, false)

        // Setup history map.
        val historyGraph: GraphView = viewOfLayout.findViewById(R.id.mapHistoryGraph)
        historyGraph.viewTreeObserver.addOnGlobalLayoutListener(GetMapLocation())

        // Setup dropdown menu
        // Todo: Remove dummy data and populate spinner with actual data fetched from the backend.
        val spinner: Spinner = viewOfLayout.findViewById(R.id.moverHistorySpinner)
        val testArray = resources.getStringArray(R.array.test_array)
        val adapter = activity?.applicationContext?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, testArray) }
        spinner.adapter = adapter

        return viewOfLayout
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