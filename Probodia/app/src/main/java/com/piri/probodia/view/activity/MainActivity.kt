package com.piri.probodia.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.adapter.MainPagerAdapter
import com.piri.probodia.databinding.ActivityMainBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.view.fragment.UpdateApplicationFragment
import com.piri.probodia.view.fragment.record.SearchFoodFragment
import com.piri.probodia.viewmodel.MainViewModel
import com.piri.probodia.widget.utils.BottomSearchFood

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

        viewModel.getVersionRunnable(PreferenceRepository(applicationContext))
        viewModel.versionRunnable.observe(this) {
            if (it) {
                binding.runnableLayout.visibility = View.GONE
            } else {
                popUpUpdateApplication()
            }
        }

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

                if (item.itemId == R.id.item_record) {
                    binding.foodSearchLayout.visibility = View.VISIBLE
                } else {
                    binding.foodSearchLayout.visibility = View.GONE
                }
                true
            }
        }

        binding.foodSearchBtn.setOnClickListener {
            val fragment = SearchFoodFragment(R.integer.search)
            fragment.show(supportFragmentManager, fragment.tag)
        }

        initMainViewPager()
    }

    private fun initMainViewPager() {
        if (binding.foodSearchLayout.width > 0) {
            _initMainViewPager()
        } else {
            binding.foodSearchLayout.viewTreeObserver.addOnGlobalLayoutListener (
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding.foodSearchLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        _initMainViewPager()
                    }
                }
            )
        }
    }

    private fun _initMainViewPager() {
        BottomSearchFood.setBottomPadding(binding.foodSearchLayout.height)
        binding.mainViewpager.adapter = MainPagerAdapter(supportFragmentManager, lifecycle)
    }

    private fun popUpUpdateApplication() {
        val fragment = UpdateApplicationFragment()
        fragment.show(supportFragmentManager, fragment.tag)
    }
}