package com.example.myapplication

import android.graphics.Color
import android.graphics.Paint

val blackDot =
    Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.FILL
    }

val blackLine =
    Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

val redDot =
    Paint().apply {
        isAntiAlias = true
        color = Color.RED
        style = Paint.Style.FILL
    }

val greenDot =
    Paint().apply {
        isAntiAlias = true
        color = Color.GREEN
        style = Paint.Style.FILL
    }