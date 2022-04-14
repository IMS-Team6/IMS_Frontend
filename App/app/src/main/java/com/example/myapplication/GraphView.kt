package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver


class GraphView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val dataSet = mutableSetOf<DataPoint>()

    private val dataPointPaint = Paint().apply {
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        dataSet.forEach() { dataPoint ->
            canvas.drawCircle(dataPoint.xVal.toFloat(), dataPoint.yVal.toFloat(), 10f, dataPointPaint)
        }
    }

    fun setData(newDataSet: List<DataPoint>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
    }
}

data class DataPoint(
    var xVal: Int,
    var yVal: Int,
)