package com.example.probodia.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.probodia.R
import com.example.probodia.adapter.RecordTodayAdapter
import com.example.probodia.data.remote.model.RecordDatasBase
import com.example.probodia.data.remote.model.SortationDto
import com.example.probodia.data.remote.model.TodayRecord
import com.example.probodia.databinding.FragmentRecordPastBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.RecordPastViewModel
import com.example.probodia.widget.utils.TimeTag
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.core.widget.NestedScrollView
import androidx.annotation.NonNull


class RecordPastFragment : Fragment() {

    private lateinit var binding : FragmentRecordPastBinding
    private lateinit var viewModel : RecordPastViewModel
    private var recordRVAdapter : RecordTodayAdapter? = null

    private var dateTime = LocalDateTime.now()
    private var loadCnt = 0
    private lateinit var reloadRecord : () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_past, container, false)

        viewModel = ViewModelProvider(this).get(RecordPastViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        recordRVAdapter = RecordTodayAdapter(true)
        binding.recordRv.adapter = recordRVAdapter
        binding.recordRv.layoutManager = LinearLayoutManager(context)

        viewModel.result.observe(viewLifecycleOwner, {
            loadCnt -= 1
            val record = TodayRecord(it.second)
            val dataSet : MutableList<RecordDatasBase> =
                mutableListOf(SortationDto("SORTATION", SortationDto.Record(it.first.second, it.first.first.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), record.getDatas().size)))
            dataSet.addAll(record.getDatas())
            recordRVAdapter!!.addDataSet(dataSet)
            recordRVAdapter!!.notifyDataSetChanged()
        })

        recordRVAdapter!!.setOnItemClickListener(object : RecordTodayAdapter.OnItemClickListener {
            override fun onItemClick(data: RecordDatasBase) {
                val recordDetailFragment = RecordDetailFragment(data, reloadRecord)
                recordDetailFragment.show(parentFragmentManager, recordDetailFragment.tag)
            }
        })

        binding.recordRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager : LinearLayoutManager = binding.recordRv.layoutManager as LinearLayoutManager
                val totalCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

                if (lastVisible >= totalCount - 1 && loadCnt == 0) {
                    dateTime = dateTime.minusDays(1)
                    loadPastRecord(dateTime)
                }
            }
        })

        restartRecord()

        viewModel.isError.observe(requireActivity()) {
            Toast.makeText(requireContext(), "인터넷 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    fun loadPastRecord(dateTime : LocalDateTime) {
        loadCnt += 3
        viewModel.getPastRecord(PreferenceRepository(requireContext()), dateTime)
    }

    fun restartRecord() {
        if (recordRVAdapter != null) {
            recordRVAdapter!!.resetDataSet()
            dateTime = LocalDateTime.now()
            loadPastRecord(dateTime)
            dateTime = dateTime.minusDays(1)
            loadPastRecord(dateTime)
        }
    }

    fun setReload(reloadFunc : () -> Unit) {
        reloadRecord = reloadFunc
    }
}