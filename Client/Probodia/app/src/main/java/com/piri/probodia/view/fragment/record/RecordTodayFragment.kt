package com.piri.probodia.view.fragment.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.piri.probodia.R
import com.piri.probodia.adapter.RecordTodayAdapter
import com.piri.probodia.data.remote.model.RecordDatasBase
import com.piri.probodia.data.remote.model.RecordEmptyDto
import com.piri.probodia.data.remote.model.SortationDto
import com.piri.probodia.data.remote.model.TodayRecord
import com.piri.probodia.databinding.FragmentRecordTodayBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.RecordHistoryViewModel
import com.piri.probodia.widget.utils.BottomSearchFood
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordTodayFragment @JvmOverloads constructor(val record : (sortation : SortationDto, kind : Int) -> Unit) : Fragment() {

    private lateinit var binding: FragmentRecordTodayBinding
    private lateinit var viewModel : RecordHistoryViewModel
    private var recordRVAdapter: RecordTodayAdapter? = null

    private lateinit var reloadRecord : () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_today, container, false)

        viewModel = ViewModelProvider(this).get(RecordHistoryViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        recordRVAdapter = RecordTodayAdapter(false)
        binding.recordRv.adapter = recordRVAdapter
        binding.recordRv.layoutManager = LinearLayoutManager(context)
        binding.recordRv.setPadding(0, 0, 0, BottomSearchFood.getBottomPadding())

        viewModel.result.observe(viewLifecycleOwner) {
            val record = TodayRecord(it.second)
            val dataSet : MutableList<RecordDatasBase> =
                mutableListOf(SortationDto("SORTATION", SortationDto.Record(it.first.second, it.first.first.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")))))
            if (record.getDatas().size == 0) {
                dataSet.add(RecordEmptyDto("EMPTY"))
            } else {
                dataSet.addAll(record.getDatas())
            }
            recordRVAdapter!!.addDataSet(dataSet)
            recordRVAdapter!!.notifyDataSetChanged()
        }
        loadTodayRecord()

        recordRVAdapter!!.setOnItemClickListener(object : RecordTodayAdapter.OnItemClickListener {
            override fun onItemClick(data: RecordDatasBase) {
                val recordDetailFragment = RecordDetailFragment(data, reloadRecord)
                recordDetailFragment.show(parentFragmentManager, recordDetailFragment.tag)
            }

            override fun onRecordClick(data: SortationDto, kind: Int) {
                record(data, kind)
            }
        })

        viewModel.isError.observe(requireActivity()) {
            Toast.makeText(requireContext(), "인터넷 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    fun loadTodayRecord() {
        if (recordRVAdapter != null && ::viewModel.isInitialized) {
            recordRVAdapter!!.resetDataSet()
            val dateTime = LocalDateTime.now()
            viewModel.getRecordHistory(PreferenceRepository(requireContext()), dateTime)
        }
    }

    fun setReload(reloadFunc : () -> Unit) {
        reloadRecord = reloadFunc
    }
}