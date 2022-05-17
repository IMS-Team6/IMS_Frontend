package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class ImageFragment: Fragment(R.layout.fragment_image) {
    private lateinit var viewOfLayout: View
    private lateinit var client: OkHttpClient
    private lateinit var imageObjects: List<CollisionInfo>
    private lateinit var imgView: ImageView

    private var sessionId: String = ""
    private val baseURL: String = "http://3.72.195.76/api/download/collisionimg/"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_image, container, false)

        // Create OkHttp Client.
        client = OkHttpClient()

        // Setup ImageView
        imgView = viewOfLayout.findViewById(R.id.imageView)

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

                // TODO: Change image based on user selection.
                showImageFromApi("$baseURL$sessionId/$selectedImage")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Spinner", "Nothing!")
            }
        }
    }

    private fun showImageFromApi(sUrl: String) {
        // TODO: Add check and update user if fetching of the image was successful or not.
        Log.d("testImage", "Fetching image from $sUrl")
        val image = Picasso.get().load(sUrl).into(imgView)
    }

    private fun fetchImage(sUrl: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)

            if (result != null) {
                try {
                    withContext(Dispatchers.Main) {
                        Log.d("fetchImage", "Success!")

                    }
                } catch (error: java.lang.Error) {
                    Toast.makeText(activity,error.toString(), Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("ERROR", "Request returned no response!")
                Toast.makeText(activity,"Request returned no response!",Toast.LENGTH_SHORT).show()
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