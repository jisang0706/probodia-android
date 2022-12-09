package com.piri.probodia.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class SquareLinearLayout : LinearLayout {
    constructor(context : Context?) : super(context!!)
    constructor(context : Context?, attrs : AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context : Context?, attrs : AttributeSet?, defStyle : Int) : super(
        context!!, attrs, defStyle
    )

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec, widthMeasureSpec
        )
    }
}