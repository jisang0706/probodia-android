package com.piri.probodia.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.adapter.MainPagerAdapter
import com.piri.probodia.databinding.ActivityMainBinding
import com.piri.probodia.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.mainViewpager.adapter = MainPagerAdapter(supportFragmentManager, lifecycle)
        binding.mainViewpager.isUserInputEnabled = false

        binding.bottomNavigation.run {
            setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.item_record -> binding.mainViewpager.currentItem = 0
                    R.id.item_challenge -> binding.mainViewpager.currentItem = 1
                    R.id.item_community -> binding.mainViewpager.currentItem = 2
                    R.id.item_etc -> binding.mainViewpager.currentItem = 3
                    else -> true
                }
                true
            }
        }
    }
}