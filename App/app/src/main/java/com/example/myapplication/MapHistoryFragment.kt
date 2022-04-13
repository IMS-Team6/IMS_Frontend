package com.example.myapplication

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import java.util.*
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
        historyGraph.setData(generateRandomDataPoints())

        // Setup dropdown menu
        // Todo: Remove dummy data and populate spinner with actual data fetched from the backend.
        val spinner: Spinner = viewOfLayout.findViewById(R.id.moverHistorySpinner)
        val testArray = resources.getStringArray(R.array.test_array)
        val adapter = activity?.applicationContext?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, testArray) }
        spinner.adapter = adapter

        return viewOfLayout
    }

    private fun generateRandomDataPoints(): List<DataPoint> {

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        val screenHorizontalCenter = screenWidth / 2
        val screenVerticalCenter = screenHeight / 2

        val list = mutableListOf<DataPoint>()

        val random = Random()

        for (i in 0..50) {
            val xVal = random.nextInt(100) + screenHorizontalCenter
            val yVal = random.nextInt(100) + screenVerticalCenter

            list.add(DataPoint(xVal, yVal))
        }

        return list
    }

}