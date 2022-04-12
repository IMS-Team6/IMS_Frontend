package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner

class MapHistoryFragment : Fragment() {
    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_map_history, container, false)

        val spinner: Spinner = viewOfLayout.findViewById(R.id.moverHistorySpinner)

        // Todo: Remove dummy data and populate spinner with actual data fetched from the backend.
        val testArray = resources.getStringArray(R.array.test_array)
        val adapter = activity?.applicationContext?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, testArray) }
        spinner.adapter = adapter

        return viewOfLayout
    }



}