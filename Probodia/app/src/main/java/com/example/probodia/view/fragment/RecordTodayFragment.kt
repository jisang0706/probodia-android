package com.example.probodia.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

class RecordTodayFragment : Fragment() {

    private lateinit var binding: FragmentRecordTodayBinding
    private lateinit var viewModelFactory : RecordTodayViewModelFactory
    private lateinit var viewModel : RecordTodayViewModel
    private var recordRVAdapter: RecordTodayAdapter? = null

    private lateinit var reloadRecord : () -> Unit

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

        recordRVAdapter = RecordTodayAdapter(false)
        binding.recordRv.adapter = recordRVAdapter
        binding.recordRv.layoutManager = LinearLayoutManager(context)

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val todayRecord = TodayRecord(it.second)
            val dataSet : MutableList<RecordDatasBase> =
                mutableListOf(SortationDto("SORTATION", SortationDto.Record(it.first,  "2022-01-01", todayRecord.getDatas().size)))
            dataSet.addAll(todayRecord.getDatas())
            recordRVAdapter!!.addDataSet(dataSet)
            recordRVAdapter!!.notifyDataSetChanged()
        })
        loadTodayRecord()

        recordRVAdapter!!.setOnItemClickListener(object : RecordTodayAdapter.OnItemClickListener {
            override fun onItemClick(data: RecordDatasBase) {
                val recordDetailFragment = RecordDetailFragment(data, reloadRecord)
                recordDetailFragment.show(parentFragmentManager, recordDetailFragment.tag)
            }

        })

        viewModel.isError.observe(requireActivity()) {
            Toast.makeText(requireContext(), "인터넷 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    fun loadTodayRecord() {
        if (recordRVAdapter != null) {
            recordRVAdapter!!.resetDataSet()
            for (flag in 0..2) {
                viewModel.getTodayRecord(TimeTag.timeTag[flag])
            }
        }
    }

    fun setReload(reloadFunc : () -> Unit) {
        reloadRecord = reloadFunc
    }
}