package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class ImageFragment: Fragment(R.layout.fragment_image) {
    private lateinit var viewOfLayout: View
    private lateinit var client: OkHttpClient
    private lateinit var imageObjects: List<CollisionInfo>

    private var sessionId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_image, container, false)

        // Create OkHttp Client.
        client = OkHttpClient()

        // Fetch image objects from MainActivity.
        imageObjects = (activity as MainActivity).getCollisionImageObjects()
        Log.d("ImageFragment", "imageObjects: ${imageObjects.size}")

        populateSpinnerWithImageIds()

        return viewOfLayout
    }

    private fun populateSpinnerWithImageIds() {
        val imageNames = arrayListOf<String>()

        // Store session id in global variable.
        sessionId = imageObjects[0].sessionID

        // Iterate through each image objects and add each
        for (imgObject in imageObjects) {
            imageNames.add(imgObject.imgName)
        }

        val spinner: Spinner = viewOfLayout.findViewById(R.id.collisionImageSpinner)
        spinner.adapter = context?.let { ArrayAdapter(it, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, imageNames) }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                val selectedImage = selectedItem.toString()
                Log.d("selected image", selectedImage)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Spinner", "Nothing!")
            }
        }
    }

    private fun getRequest(sUrl: String): String? {
        var result: String? = null

        try {
            // Create URL
            val url = URL(sUrl)

            // Build request
            val request = Request.Builder().url(url).build()

            // Execute request
            val response = client.newCall(request).execute()
            result = response.body?.string()
        } catch (error: Error) {
            Log.d("ERROR", error.toString())
        }

        return result
    }
}