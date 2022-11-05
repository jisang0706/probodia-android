package com.piri.probodia.view.fragment.etc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.piri.probodia.R
import com.piri.probodia.adapter.ShowDoctorListAdapter
import com.piri.probodia.data.remote.model.PressureDto
import com.piri.probodia.data.remote.model.RecordDatasBase
import com.piri.probodia.data.remote.model.ShowDoctorDto
import com.piri.probodia.databinding.FragmentShowDoctorPressureBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.ShowDoctorViewModel
import com.piri.probodia.widget.utils.Convert
import java.time.LocalDateTime

class ShowDoctorPressureFragment : Fragment() {

    private lateinit var binding: FragmentShowDoctorPressureBinding
    private lateinit var viewModel: ShowDoctorViewModel
    private lateinit var showDoctorRVAdapter: ShowDoctorListAdapter

    private var minItemCount = 0
    private var lastItemDate = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_show_doctor_pressure,
            container,
            false
        )
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(ShowDoctorViewModel::class.java)
        binding.vm = viewModel

        showDoctorRVAdapter = ShowDoctorListAdapter(::setItemMinCount)
        binding.recordRv.adapter = showDoctorRVAdapter
        binding.recordRv.layoutManager = LinearLayoutManager(context)

        viewModel.localDateTime.observe(viewLifecycleOwner) {
            viewModel.getPressureRecord(PreferenceRepository(requireContext()), it)
        }

        viewModel.result.observe(viewLifecycleOwner) { result ->
            addResult(result.second)
        }

        binding.recordRv.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager: LinearLayoutManager =
                        binding.recordRv.layoutManager as LinearLayoutManager
                    val totalCount = layoutManager.itemCount
                    val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

                    val listLastItem: ShowDoctorDto? =
                        showDoctorRVAdapter.getLastItem()

                    if (listLastItem != null && lastVisible >= totalCount - 1 &&
                        lastItemDate != listLastItem.date
                    ) {
                        lastItemDate = listLastItem.date

                        viewModel.setNextLocalDateTime()
                    }
                }
            }
        )

        setStartDateTime()

        return binding.root
    }

    private fun setStartDateTime() {
        viewModel.setLocalDateTime(LocalDateTime.now().plusMonths(1).withDayOfMonth(1))
    }

    private fun addResult(result : MutableList<RecordDatasBase>) {
        val pressureList = result as MutableList<PressureDto>
        pressureList.sortByDescending {
            it.record.recordDate
        }

        val groupPressure = pressureList.groupBy { it.record.recordDate.split(' ')[0] }.values

        val showDoctorList = buildList {
            for (item in groupPressure) {
                val date = item[0].record.recordDate.split(' ')[0].split('-')
                val showDoctorDto = ShowDoctorDto("${date[1]}.${date[2]}", MutableList(6) { -1 })
                for (pressure in item) {
                    showDoctorDto.values[(Convert.timeTagToInt(pressure.record.timeTag) - 6) * 2] = pressure.record.maxPressure
                    showDoctorDto.values[(Convert.timeTagToInt(pressure.record.timeTag) - 6) * 2 + 1] = pressure.record.minPressure
                }
                add(showDoctorDto)
            }
        }.toMutableList()

        showDoctorRVAdapter.addDataSet(showDoctorList)
        showDoctorRVAdapter.notifyDataSetChanged()

        if (minItemCount >= showDoctorRVAdapter.itemCount) {
            viewModel.setNextLocalDateTime()
        }
    }

    private fun setItemMinCount(itemHeight : Int) {
        minItemCount = Math.ceil(binding.recordRv.height.toDouble() / itemHeight).toInt()
    }
}