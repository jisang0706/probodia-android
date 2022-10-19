package com.piri.probodia.widget.utils

import android.content.res.Resources

object Convert {

    private val timeTagMap : Map<String, Int> = buildMap {
        put("아침 식전", 0)
        put("아침 식후", 1)
        put("점심 식전", 2)
        put("점심 식후", 3)
        put("저녁 식전", 4)
        put("저녁 식후", 5)
        put("아침", 6)
        put("점심", 7)
        put("저녁", 8)
    }

    private val timeTagList = buildList {
        add("아침 식전")
        add("아침 식후")
        add("점심 식전")
        add("점심 식후")
        add("저녁 식전")
        add("저녁 식후")
        add("아침")
        add("점심")
        add("저녁")
    }

    fun floatToDp(num : Float) : Int {
        return (num * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
    }

    fun timeTagToInt(timeTag : String) : Int {
        return timeTagMap.get(timeTag)!!
    }

    fun getTimeTag(i : Int) : String {
        return timeTagList[i]
    }
}