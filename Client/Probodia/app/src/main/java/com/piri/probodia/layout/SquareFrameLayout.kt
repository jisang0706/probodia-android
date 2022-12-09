package com.piri.probodia.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class SquareFrameLayout : FrameLayout {
    constructor(context : Context?) : super(context!!)
    constructor(context : Context?, attrs : AttributeSet?) : super(
        context!!, attrs
            )

    constructor(context : Context?, attrs : AttributeSet?, defStyle : Int) : super(
        context!!, attrs, defStyle
            )

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            Integer.max(heightMeasureSpec, widthMeasureSpec),
            Integer.max(heightMeasureSpec, widthMeasureSpec)
        )

    }
}