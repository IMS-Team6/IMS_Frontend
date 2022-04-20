package com.example.myapplication

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationItemView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

        /*val appBarConfiguration = AppBarConfiguration(setOf(R.id.connect_Fragment, R.id.control_Fragment, R.id.mapHistory_Fragment))
        setupActionBarWithNavController(navController, appBarConfiguration)*/
        bottomNavigationItemView.setupWithNavController(navController)

        // Create BluetoothManager
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        val bluetoothConnectBtn: Button = findViewById(R.id.bluetoothConnectButton)

        bluetoothConnectBtn.setOnClickListener {
            // Check if device supports bluetooth
            if (bluetoothAdapter == null) {
                Log.d("bluetooth", "Device does not support bluetooth.")
            } else {
                Log.d("bluetooth", "Bluetooth is supported.")

                // Check if bluetooth is enabled
                if (!bluetoothAdapter.isEnabled) {
                    // Bluetooth is not enabled.
                    Log.d("bluetooth", "Bluetooth is not enabled.")
                    setupBluetoothPermissions()
                } else {
                    // Bluetooth is enabled!
                    Log.d("bluetooth", "Bluetooth is enabled.")
                }
            }
        }
    }

    private fun setupBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT))
        }
        else{
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }
    }

    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            //granted
            Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show()
        }else{
            //deny
            Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
            }
        }
}