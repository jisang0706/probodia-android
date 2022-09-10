package com.example.probodia.view.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.databinding.ActivityJoinBaseInfoBinding
import com.example.probodia.viewmodel.JoinBaseInfoViewModel

class JoinBaseInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinBaseInfoBinding
    private lateinit var viewModel : JoinBaseInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join_base_info)

        viewModel = ViewModelProvider(this).get(JoinBaseInfoViewModel::class.java)
        binding.vm = viewModel

        binding.genderSelectLayout.firBtn.text = "여성"
        binding.genderSelectLayout.secBtn.text = "남성"
        binding.genderSelectLayout.firBtn.visibility = View.GONE

        binding.genderSelectLayout.firBtn.setBackgroundResource(R.drawable.primary_100_1_background)
        binding.genderSelectLayout.firBtn.setTextColor(Color.WHITE)
    }
}