package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.ParcelUuid
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.IOException
import java.lang.reflect.Method
import java.util.*


class MainActivity : AppCompatActivity() {

    private val moverMacAddress: String = "B8:27:EB:21:8A:D4"
    private val moverUuId: String = "7be1fcb3-5776-42fb-91fd-2ee7b5bbb86d"
    
    private val receiver = object: BroadcastReceiver() {
        @SuppressLint("MissingPermission")

        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    Log.d("Bluetooth Status", "Device found!")

                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address
                    val id = device?.uuids
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Register for broadcasts when a device is discovered
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

        val bottomNavigationItemView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

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
                    setupBluetoothPermissions()
                } else {
                    // Bluetooth is enabled!
                    val btDevice = bluetoothAdapter.getRemoteDevice(moverMacAddress)
                    val btConnectionThread = BtConnectThread(btDevice)
                    btConnectionThread.run(bluetoothAdapter)

                    //getPairedDevices(bluetoothAdapter)

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
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

    @SuppressLint("MissingPermission")
    private fun getPairedDevices(bluetoothAdapter: BluetoothAdapter) {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        pairedDevices?.forEach { device ->
            val uuIds = device.uuids

            Log.d("Device", "Name: ${device.name} | Address: ${device.address}")

            uuIds.forEach { id ->
                Log.d("Test", "uuid: $id")
            }
        }
    }

    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            //granted
            Log.d("Bluetooth Status -->", "Bluetooth permission granted")
        }else{
            //deny
            Log.d("Bluetooth Status -->", "Bluetooth permission denied")
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
            }
        }

    private fun manageMyConnectedSocket(socket: BluetoothSocket) {
        // TODO: Manage connection.
    }

    @SuppressLint("MissingPermission")
    private inner class BtConnectThread(device: BluetoothDevice) : Thread() {

        private val btDeviceAddress = UUID.fromString(moverUuId)

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(btDeviceAddress)
        }

        fun run(bluetoothAdapter: BluetoothAdapter) {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery()

            mmSocket.let { socket ->
                if (socket != null) {
                    Log.d("Status:", "Connecting to device...")
                    // Connect to the remote device through the socket. This call blocks
                    // until it succeeds or throws an exception.
                    socket.connect()

                    // The connection attempt succeeded. Perform work associated with
                    // the connection in a separate thread.
                    manageMyConnectedSocket(socket)
                }
            }
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }
}