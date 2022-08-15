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
import com.example.probodia.data.remote.model.RecordDatasBase
import com.example.probodia.data.remote.model.SortationDto
import com.example.probodia.data.remote.model.TodayRecord
import com.example.probodia.databinding.FragmentRecordTodayBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.RecordTodayViewModel
import com.example.probodia.viewmodel.factory.RecordTodayViewModelFactory
import com.example.probodia.widget.utils.TimeTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordTodayFragment : Fragment() {

    private lateinit var binding: FragmentRecordTodayBinding
    private lateinit var viewModelFactory : RecordTodayViewModelFactory
    private lateinit var viewModel : RecordTodayViewModel
    private lateinit var recordRVAdapter: RecordTodayAdapter

    private var preLast = 0
    private var flag = 0

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

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val todayRecord = TodayRecord(it.second)
            val dataSet : MutableList<RecordDatasBase> =
                mutableListOf(SortationDto("SORTATION", SortationDto.Record(it.first,  "2022-08-14", todayRecord.getDatas().size)))
            dataSet.addAll(todayRecord.getDatas())
            recordRVAdapter.addDataSet(dataSet)
            recordRVAdapter.notifyDataSetChanged()
            if (flag < 3) {
                viewModel.getTodayRecord(TimeTag.timeTag[flag++])
            }
        })
        loadTodayRecord()

        return binding.root
    }

    fun loadTodayRecord() {
        recordRVAdapter.resetDataSet()
        flag = 0
        viewModel.getTodayRecord(TimeTag.timeTag[flag++])
    }
}