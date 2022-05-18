package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso


class ImageFragment: Fragment(R.layout.fragment_image) {
    private lateinit var viewOfLayout: View
    private lateinit var imageObjects: List<CollisionInfo>
    private lateinit var imgView: ImageView
    private lateinit var txtView: TextView

    private var sessionId: String = ""
    private val baseURL: String = "http://3.72.195.76/api/download/collisionimg/"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_image, container, false)

        // Setup ImageView
        imgView = viewOfLayout.findViewById(R.id.imageView)
        txtView = viewOfLayout.findViewById(R.id.imageFragmentTitle)

        // Fetch image objects from MainActivity.
        imageObjects = (activity as MainActivity).getCollisionImageObjects()

        // Store session id in global variable and add to TextView.
        sessionId = imageObjects[0].sessionID
        txtView.text = "Session: $sessionId"

        // Setup spinner.
        populateSpinnerWithImageIds()

        return viewOfLayout
    }

    private fun populateSpinnerWithImageIds() {
        val imageNames = arrayListOf<String>()

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

                showImageFromApi("$baseURL$sessionId/$selectedImage")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Spinner", "Nothing Selected!")
            }
        }
    }

    private fun showImageFromApi(sUrl: String) {
        // Sends GET request to API and displays adds fetched image to ImageView.
        Picasso.get().load(sUrl).into(imgView)
    }
}