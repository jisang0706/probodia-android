package com.piri.probodia.widget.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AdaptiveSpacingItemDecoration(
    private val size : Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = size * 2
        outRect.right = size * 2
        outRect.top = size * 2
        outRect.bottom = size * 2
    }
}