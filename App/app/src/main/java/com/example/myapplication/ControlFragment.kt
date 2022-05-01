package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_control.*

class ControlFragment : Fragment(R.layout.fragment_control) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controlSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                (activity as MainActivity?)?.writeData(8); // Auto
            else
                (activity as MainActivity?)?.writeData(9); // Manuel
        }

        controlUp.setOnClickListener {
            (activity as MainActivity?)?.writeData(1);
        }

        controlLeft.setOnClickListener {
            (activity as MainActivity?)?.writeData(2);
        }

        controlRight.setOnClickListener {
            (activity as MainActivity?)?.writeData(3);
        }

        controlDown.setOnClickListener {
            (activity as MainActivity?)?.writeData(4);
        }
    }
}