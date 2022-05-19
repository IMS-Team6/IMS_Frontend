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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
    @RequiresApi(Build.VERSION_CODES.S)
    var sdk31Permissions = arrayOf(
    Manifest.permission.BLUETOOTH_SCAN,
    Manifest.permission.BLUETOOTH_CONNECT,
    Manifest.permission.BLUETOOTH_ADMIN)

    var sdk30Permissions =arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN
    )
    var sdk28Permissions =arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN
    )
}

class MainActivity : AppCompatActivity() {
    private val moverMacAddress: String = "B8:27:EB:21:8A:D4"
    private val moverUuId: String = "7be1fcb3-5776-42fb-91fd-2ee7b5bbb86d"
    var btConnectionThread: BtConnectThread? = null;

    private lateinit var bluetoothStatusText: TextView

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
                    Log.d("bluetooth", "$deviceName $deviceHardwareAddress")
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup bottom navigation bar.
        val bottomNavigationItemView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)
        bottomNavigationItemView.setupWithNavController(navController)

        // Hide title bar.
        supportActionBar?.hide()

        // Setup textview
        bluetoothStatusText = findViewById(R.id.btTextStatus)
        updateBtConnectionStatus()

        // Create BluetoothManager
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        // Setup bluetooth connect button.
        val bluetoothConnectBtn: Button = findViewById(R.id.bluetoothConnectButton)

        bluetoothConnectBtn.setOnClickListener {
            // Check if device supports bluetooth
            if (bluetoothAdapter == null) {
                Toast.makeText(applicationContext,getString(R.string.error_msg_bt_not_supported),Toast.LENGTH_SHORT).show()
            } else {
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
                    }
                }
            }
        }
    }

    private fun updateBtConnectionStatus() {
        if (checkConnectionStatus()) {
            imageView2.setImageResource(R.drawable.rb_connected)
            bluetoothStatusText.text = getString(R.string.bluetooth_connected)
        } else {
            imageView2.setImageResource(R.drawable.not_connected)
            bluetoothStatusText.text = getString(R.string.bluetooth_not_connected)
        }
    }

    fun checkConnectionStatus(): Boolean {
        return btConnectionThread != null && btConnectionThread!!.isConnected()
    }

    fun getCollisionImageObjects(): List<CollisionInfo> {
        return collisionImageObjects
    }

    fun updateCollisionImageObjects(colImgObjects: List<CollisionInfo>) {
        collisionImageObjects = colImgObjects as MutableList<CollisionInfo>
    }

    private fun checkPermissions(): Boolean {
        var result = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            PermissionsBasedOnSDKVersion.sdk31Permissions.forEach {
                result = ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
                if(!result)
                    return  result;
            }
        }
        else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            PermissionsBasedOnSDKVersion.sdk30Permissions.forEach {
                result = ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
                if(!result)
                    return  result;
            }
        }
        else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            PermissionsBasedOnSDKVersion.sdk28Permissions.forEach {
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
        else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            requestMultiplePermissions.launch(PermissionsBasedOnSDKVersion.sdk30Permissions)
        }
        else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            requestMultiplePermissions.launch(PermissionsBasedOnSDKVersion.sdk28Permissions)
        }
        else{
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }
    }

    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Bluetooth request granted.
            bluetoothStatusText.text = getString(R.string.bluetooth_connected)
        }else{
            // Bluetooth request denied.
            bluetoothStatusText.text = getString(R.string.bluetooth_not_connected)
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
            }
        }


    fun writeData(data: Int) {
        if(btConnectionThread != null && btConnectionThread!!.isConnected()) {
            btConnectionThread!!.writeData(data)
        } else {
            Toast.makeText(applicationContext,getString(R.string.error_msg_bt_not_connected),Toast.LENGTH_SHORT).show()
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

        fun isConnected(): Boolean {
            return this::mmOutStream.isInitialized
        }

        fun writeData(data: Int) {
            try {
                val byteArr: ByteArray = ByteBuffer.allocate(4).putInt(data).array()
                mmOutStream.write(byteArr);
            } catch (error: java.lang.Exception) {
                Log.e("bluetooth", error.toString())
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
                        if(socket.isConnected) {
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
            } catch (error: IOException) {
                Log.e(TAG, error.toString())
            }
        }
    }
}