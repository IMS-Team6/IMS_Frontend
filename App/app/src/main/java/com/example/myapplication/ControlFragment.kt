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

        val btConnectionThread = (activity as MainActivity).btConnectionThread;

        if(btConnectionThread != null && btConnectionThread.isConnected()) {
            bluetoothConnectText.setText(R.string.bluetoothConnected);
            Log.i("Bluetooth", "Bluetooth Connected!")
        } else {
            bluetoothConnectText.setText(R.string.bluetoothNotConnected);
            Log.i("Bluetooth", "Bluetooth not connected!");
        }

        controlSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                (activity as MainActivity?)?.writeData(8); // Auto
            else
                (activity as MainActivity?)?.writeData(9); // Manuel
        }


        controlUp.setOnTouchListener { _, event ->              //Forward
            if (event.action == MotionEvent.ACTION_DOWN) {
                Log.d("kalle", "true!")
                (activity as MainActivity?)?.writeData(1);
            } else if (event.action == MotionEvent.ACTION_UP) {
                Log.d("kalle", "false!")
                (activity as MainActivity?)?.writeData(0);
            }
            false
        }

        controlLeft.setOnTouchListener { _, event ->              //Left
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