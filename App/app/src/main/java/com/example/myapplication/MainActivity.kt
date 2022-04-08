package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val frgConnect = ConnectFragment()
        val frgControl = ControlFragment()
        val frgMapHistory = MapHistoryFragment()

        // Set default fragment (ConnectFragment)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, frgConnect)
            commit()
        }

        // Change to ConnectFragment
        btnConnectFragment.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentHolder, frgConnect)
                addToBackStack(null)
                commit()
            }
        }

        // Change to ControlFragment
        btnControlFragment.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentHolder, frgControl)
                addToBackStack(null)
                commit()
            }
        }

        // Change to MapHistoryFragment
        btnMapHistoryFragment.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentHolder, frgMapHistory)
                addToBackStack(null)
                commit()
            }
        }

    }
}