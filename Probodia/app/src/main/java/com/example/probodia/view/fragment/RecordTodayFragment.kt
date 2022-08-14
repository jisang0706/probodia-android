package com.example.probodia.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probodia.R
import com.example.probodia.adapter.RecordTodayAdapter
import com.example.probodia.databinding.FragmentRecordTodayBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.RecordTodayViewModel
import com.example.probodia.viewmodel.factory.RecordTodayViewModelFactory

class RecordTodayFragment : Fragment(), AbsListView.OnScrollListener {

    private lateinit var binding: FragmentRecordTodayBinding
    private lateinit var viewModelFactory : RecordTodayViewModelFactory
    private lateinit var viewModel : RecordTodayViewModel
    private lateinit var recordRVAdapter: RecordTodayAdapter

    private var preLast = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_today, container, false)

        viewModelFactory = RecordTodayViewModelFactory(PreferenceRepository(requireContext()))
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecordTodayViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        recordRVAdapter = RecordTodayAdapter()
        binding.recordRv.adapter = recordRVAdapter
        binding.recordRv.layoutManager = LinearLayoutManager(context)

        viewModel.getTodayRecord()

        viewModel.result.observe(viewLifecycleOwner, Observer {
            recordRVAdapter.addDataSet(it)
            recordRVAdapter.notifyDataSetChanged()
        })

        return binding.root
    }

    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {

    }

    override fun onScroll(
        listView: AbsListView?,
        firstVisibleItem: Int,
        visibleItemCount: Int,
        totalItemCount: Int
    ) {
        when (listView!!.id) {
            R.id.record_rv -> {
                val lastItem = firstVisibleItem + visibleItemCount

                if (lastItem == recordRVAdapter.itemCount && preLast != lastItem) {
                    preLast = lastItem
                    viewModel.getTodayRecord()
                }
            }
        }
    }

    fun reloadTodayRecord() {
        recordRVAdapter.resetDataSet()
        viewModel.reloadTodayRecord()
    }
}