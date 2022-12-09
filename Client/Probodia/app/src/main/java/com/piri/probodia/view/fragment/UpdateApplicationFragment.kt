package com.piri.probodia.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.piri.probodia.R
import com.piri.probodia.databinding.FragmentUpdateApplicationBinding

class UpdateApplicationFragment : DialogFragment() {

    private lateinit var binding : FragmentUpdateApplicationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_application, container, false)

        dialog?.window?.setBackgroundDrawableResource(R.drawable.white_pure_2_background)

        binding.enterBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=" + requireContext().packageName)
            startActivity(intent)
        }

        return binding.root
    }
}