package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_control.*

class ControlFragment : Fragment(R.layout.fragment_control) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TestButton.setOnClickListener {
            (activity as MainActivity?)?.writeData(1);
        }
    }
}