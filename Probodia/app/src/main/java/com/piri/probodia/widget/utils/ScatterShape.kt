package com.piri.probodia.widget.utils

import android.content.res.Resources
import com.github.mikephil.charting.charts.ScatterChart
import com.piri.probodia.R

object ScatterShape {

    val scatterShape = buildList<ScatterChart.ScatterShape> {
        add(ScatterChart.ScatterShape.CIRCLE)
        add(ScatterChart.ScatterShape.CROSS)
        add(ScatterChart.ScatterShape.SQUARE)
        add(ScatterChart.ScatterShape.TRIANGLE)
        add(ScatterChart.ScatterShape.X)
        add(ScatterChart.ScatterShape.CHEVRON_UP)
    }
}