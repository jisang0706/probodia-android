package com.piri.probodia.view.fragment.record

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.piri.probodia.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.util.DisplayMetrics




open class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "BottomSheetFragment"
    }

    protected fun setUpRatio(bottomSheetDialog : BottomSheetDialog, height : Int) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheeetDialogDefaultHeight(height)
        bottomSheet.layoutParams = layoutParams
        bottomSheet.setBackgroundResource(R.drawable.white_top_3_background)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheeetDialogDefaultHeight(height : Int) : Int {
        return getWindowHeight() * height / 100
    }

    private fun getWindowHeight() : Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return (context as Activity?)!!.getSystemService(WindowManager::class.java)
                .currentWindowMetrics.bounds.height()
        } else {
            val displayMetrics = DisplayMetrics()
            (context as Activity?)!!.windowManager.getDefaultDisplay().getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }
    }
}