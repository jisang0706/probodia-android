package com.example.probodia.view.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import com.example.probodia.R
import com.example.probodia.data.remote.model.RecordDatasBase
import com.example.probodia.databinding.FragmentRecordDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RecordDetailFragment(var data : RecordDatasBase) : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentRecordDetailBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_detail, container, false)

        binding.titleText.text = when(data.type) {
            "SUGAR" -> "혈당"
            "PRESSURE" -> "혈압"
            "MEDICINE" -> "투약"
            "MEAL" -> "음식"
            else -> ""
        }

        binding.cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        return binding.root
    }

    companion object {
        const val TAG = "BottomSheetFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog : Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setUpRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setUpRatio(bottomSheetDialog : BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheeetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        bottomSheet.setBackgroundResource(R.drawable.white_top_3_background)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheeetDialogDefaultHeight() : Int {
        return getWindowHeight() * 50 / 100
    }

    private fun getWindowHeight() : Int {
        return (context as Activity?)!!.getSystemService(WindowManager::class.java)
            .currentWindowMetrics.bounds.height()
    }
}