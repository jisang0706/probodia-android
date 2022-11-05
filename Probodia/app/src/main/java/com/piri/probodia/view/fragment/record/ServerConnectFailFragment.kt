package com.piri.probodia.view.fragment.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.piri.probodia.R
import com.piri.probodia.databinding.FragmentServerConnectFailBinding

class ServerConnectFailFragment(val finish : () -> Unit) : DialogFragment() {

    private lateinit var binding : FragmentServerConnectFailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_server_connect_fail, container, false)

        dialog?.window?.setBackgroundDrawableResource(R.drawable.white_pure_2_background)

        binding.enterBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        return binding.root
    }

    override fun onPause() {
        finish()
        super.onPause()
    }
}