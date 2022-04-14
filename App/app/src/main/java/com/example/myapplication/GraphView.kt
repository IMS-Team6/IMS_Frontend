package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver


class GraphView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val dataSet = mutableSetOf<DataPoint>()
    private lateinit var previousDataPoint: DataPoint
    private val lineWidth: Float = 5.0F

    private val dataPointPaint = Paint().apply {
        color = Color.RED
    }

    private val dataRoutePaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = lineWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        dataSet.forEach() { dataPoint ->
            canvas.drawCircle(dataPoint.xVal.toFloat(), dataPoint.yVal.toFloat(), 10f, dataPointPaint)

            if (this::previousDataPoint.isInitialized) {
                canvas.drawLine(previousDataPoint.xVal.toFloat(), previousDataPoint.yVal.toFloat(), dataPoint.xVal.toFloat(), dataPoint.yVal.toFloat(), dataRoutePaint)
                previousDataPoint.xVal = dataPoint.xVal
                previousDataPoint.yVal = dataPoint.yVal
            } else {
                previousDataPoint = DataPoint(dataPoint.xVal, dataPoint.yVal)
            }
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