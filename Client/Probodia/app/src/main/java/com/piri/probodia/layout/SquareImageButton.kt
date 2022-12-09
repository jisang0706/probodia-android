package com.piri.probodia.layout

import android.content.Context
import android.util.AttributeSet

class SquareImageButton : androidx.appcompat.widget.AppCompatImageButton {
    constructor(context : Context?) : super(context!!)
    constructor(context : Context, attrs : AttributeSet?) : super(context!!, attrs)

    constructor(context : Context?, attrs : AttributeSet?, defStyleAttr : Int) : super(
        context!!, attrs, defStyleAttr
            )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}