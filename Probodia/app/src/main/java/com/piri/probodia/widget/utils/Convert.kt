package com.piri.probodia.widget.utils

import android.content.res.Resources

object Convert {

    fun floatToDp(num : Float) : Int {
        return (num * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
    }
}