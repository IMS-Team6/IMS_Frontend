package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_control.*

class ControlFragment : Fragment(R.layout.fragment_control) {

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controlSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                (activity as MainActivity?)?.writeData(8); // Auto
            else
                (activity as MainActivity?)?.writeData(9); // Manual

            controlUp.isEnabled = !isChecked;
            controlLeft.isEnabled = !isChecked;
            controlRight.isEnabled = !isChecked;
            controlDown.isEnabled = !isChecked;
        }


        controlUp.setOnTouchListener { _, event ->              //Forward
            if (event.action == MotionEvent.ACTION_DOWN) {
                (activity as MainActivity?)?.writeData(1);
            } else if (event.action == MotionEvent.ACTION_UP) {
                (activity as MainActivity?)?.writeData(0);
            }
            false
        }

        controlLeft.setOnTouchListener { view, event ->              //Left
            if (event.action == MotionEvent.ACTION_DOWN) {
                (activity as MainActivity?)?.writeData(2);
            } else if (event.action == MotionEvent.ACTION_UP) {
                (activity as MainActivity?)?.writeData(0);
            }
             false
        }

        controlRight.setOnTouchListener { _, event ->              //Right
            if (event.action == MotionEvent.ACTION_DOWN) {
                (activity as MainActivity?)?.writeData(3);
            } else if (event.action == MotionEvent.ACTION_UP) {
                (activity as MainActivity?)?.writeData(0);
            }
            false
        }

        controlDown.setOnTouchListener { _, event ->              //Down
            if (event.action == MotionEvent.ACTION_DOWN) {
                (activity as MainActivity?)?.writeData(4);
            } else if (event.action == MotionEvent.ACTION_UP) {
                (activity as MainActivity?)?.writeData(0);
            }
            false

        }
    }
}