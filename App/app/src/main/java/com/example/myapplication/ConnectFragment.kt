package com.example.myapplication



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_connect.*

class ConnectFragment : Fragment(R.layout.fragment_connect) {

    private lateinit var viewOfLayout: View
    private lateinit var bluetoothStatusText: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_connect, container, false)
        bluetoothStatusText = viewOfLayout.findViewById(R.id.btTextStatus)
        return viewOfLayout
    }

    override fun onResume() {
        super.onResume()
        updateBtConnectionStatus()
    }

    private fun updateBtConnectionStatus() {
        if ((activity as MainActivity).checkConnectionStatus()) {
            imageView2.setImageResource(R.drawable.rb_connected)
            bluetoothStatusText.text = getString(R.string.bluetooth_connected)
        } else {
            imageView2.setImageResource(R.drawable.not_connected)
            bluetoothStatusText.text = getString(R.string.bluetooth_not_connected)
        }
    }
}