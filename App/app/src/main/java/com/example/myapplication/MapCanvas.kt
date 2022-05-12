package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View

class MapCanvas(context: Context, rect: Rect): View(context) {
    private lateinit var mapRect: Rect
    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}