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
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_connect.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.util.*

object PermissionsBasedOnSDKVersion {
    var sdk31Permissions = arrayOf(
    Manifest.permission.BLUETOOTH_SCAN,
    Manifest.permission.BLUETOOTH_CONNECT,
    Manifest.permission.BLUETOOTH_ADMIN)

    var sdk30Permissions =arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN
    )
}

class MainActivity : AppCompatActivity() {
    private val moverMacAddress: String = "B8:27:EB:21:8A:D4"
    private val moverUuId: String = "7be1fcb3-5776-42fb-91fd-2ee7b5bbb86d"
    var btConnectionThread: BtConnectThread? = null;

    private lateinit var collisionImageObjects: MutableList<CollisionInfo>

    private val receiver = object: BroadcastReceiver() {
        @SuppressLint("MissingPermission")

        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action

            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    Log.d("bluetooth", "Device found!")

                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address
                    val id = device?.uuids
                    Log.d("bluetooth", deviceName + " " + deviceHardwareAddress)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationItemView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

        bottomNavigationItemView.setupWithNavController(navController)

        // Create BluetoothManager
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        if(btConnectionThread != null && btConnectionThread!!.isConnected())
            imageView2.setImageResource(R.drawable.rb_connected)
        else
            imageView2.setImageResource(R.drawable.not_connected)

        val bluetoothConnectBtn: Button = findViewById(R.id.bluetoothConnectButton)

        bluetoothConnectBtn.setOnClickListener {
            // Check if device supports bluetooth
            if (bluetoothAdapter == null) {
                Log.d("bluetooth", "Device does not support bluetooth.")
            } else {
                Log.d("bluetooth", "Bluetooth is supported.")

                // Check if bluetooth is enabled
                if (!bluetoothAdapter.isEnabled) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBtIntent, 9)
                } else {
                    if(!checkPermissions()) {
                        setupBluetoothPermissions()
                    } else {
                        if(btConnectionThread != null && btConnectionThread!!.isConnected()) {
                            return@setOnClickListener;
                        }

                        // Bluetooth is enabled!
                        val btDevice = bluetoothAdapter.getRemoteDevice(moverMacAddress)
                        btConnectionThread = BtConnectThread(btDevice)
                        btConnectionThread!!.run(bluetoothAdapter)
                        Log.d("bluetooth", "connected?")
                    }
                }
            }
        }
    }

    fun getCollisionImageObjects(): List<CollisionInfo> {
        return collisionImageObjects
    }

    fun updateCollisionImageObjects(colImgObjects: List<CollisionInfo>) {
        collisionImageObjects = colImgObjects as MutableList<CollisionInfo>
    }

    private fun checkPermissions(): Boolean {
        var result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            PermissionsBasedOnSDKVersion.sdk31Permissions.forEach {
                result = ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
                if(!result)
                    return  result;
            }
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            PermissionsBasedOnSDKVersion.sdk30Permissions.forEach {
                result = ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
                if(!result)
                    return  result;
            }
        }

        return result;
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun setupBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(PermissionsBasedOnSDKVersion.sdk31Permissions)
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestMultiplePermissions.launch(PermissionsBasedOnSDKVersion.sdk30Permissions)
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

            Log.d("Bluetooth", "received res")
        }


    fun writeData(data: Int) {
        if(btConnectionThread != null && btConnectionThread!!.isConnected()) {
            btConnectionThread!!.writeData(data)
        } else {
            Toast.makeText(applicationContext,"Du är inte connectad till bluetooth",Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    inner class BtConnectThread(device: BluetoothDevice) : Thread() {

        private val btDeviceAddress = UUID.fromString(moverUuId)
        lateinit var mmInStream: InputStream;
        lateinit var mmOutStream: OutputStream;

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))//btDeviceAddress)
        }

        public fun isConnected(): Boolean {
            if(this::mmOutStream.isInitialized)
                return mmOutStream != null;
            else
                return false
        }

        public fun writeData(data: Int) {
            try {
                val byteArr: ByteArray = ByteBuffer.allocate(4).putInt(data).array()
                mmOutStream.write(byteArr);
            } catch (e: java.lang.Exception) {
                Log.e("bluetooth", "Exception during write", e)
            }
        }

        fun run(bluetoothAdapter: BluetoothAdapter) {
            try {
                // Cancel discovery because it otherwise slows down the connection.
                bluetoothAdapter.cancelDiscovery()

                mmSocket.let { socket ->
                    if (socket != null) {
                        Log.d("bluetooth", "Connecting to device...")
                        // Connect to the remote device through the socket. This call blocks
                        // until it succeeds or throws an exception.
                        socket.connect()
                        // The connection attempt succeeded. Perform work associated with
                        // the connection in a separate thread.
                        if(socket.isConnected()) {
                            Log.d("bluetooth", "Is Connected")
                            imageView2.setImageResource(R.drawable.rb_connected)
                            mmInStream = socket.inputStream
                            mmOutStream = socket.outputStream
                        }
                        //manageMyConnectedSocket(socket)
                    }
                }
            } catch ( ex: Exception) {
                Log.e("bluetooth", ex.message.toString())
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