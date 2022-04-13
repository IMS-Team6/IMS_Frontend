package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class GraphView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val dataSet = mutableSetOf<DataPoint>()

    private var xMin = 0
    private var xMax = 0
    private var yMin = 0
    private var yMax = 0

    private val dataPointPaint = Paint().apply {
        color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        dataSet.forEach() { dataPoint ->
            canvas.drawCircle(dataPoint.xVal.toFloat(), dataPoint.yVal.toFloat(), 5f, dataPointPaint)
        }
    }

    fun setData(newDataSet: List<DataPoint>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
    }

}

data class DataPoint(
    val xVal: Int,
    val yVal: Int,
)