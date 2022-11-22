package com.piri.probodia.view.fragment.etc

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.databinding.FragmentEtcBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.view.activity.JoinBaseInfoActivity
import com.piri.probodia.view.activity.PersonalInformationActivity
import com.piri.probodia.view.activity.ShowDoctorActivity
import com.piri.probodia.viewmodel.EtcViewModel

class EtcFragment : Fragment() {

    private lateinit var binding : FragmentEtcBinding
    private lateinit var viewModel : EtcViewModel
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_etc, container, false)

        viewModel = ViewModelProvider(this).get(EtcViewModel::class.java)
        binding.vm = viewModel

        viewModel.getUserId()

        viewModel.userId.observe(requireActivity(), {
            viewModel.getUserProfile(PreferenceRepository(requireContext()), it)
        })

        viewModel.userData.observe(requireActivity()) {
            binding.userNameText.text = it.username
            binding.userAgeText.text = "${it.age}세"
            binding.userBodyText.text = "${it.height}cm / ${it.weight}kg"
            binding.editBtn.visibility = View.VISIBLE
        }

        viewModel.isError.observe(requireActivity()) {
            Toast.makeText(requireContext(), "인터넷 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
        }

        binding.ruleBtn.setOnClickListener {
            val intent = Intent(requireContext(), PersonalInformationActivity::class.java)
            startActivity(intent)
        }

        binding.showBtn.setOnClickListener {
            val intent = Intent(requireContext(), ShowDoctorActivity::class.java)
            startActivity(intent)
        }

        binding.editBtn.setOnClickListener {
            val goIntent = Intent(requireContext(), JoinBaseInfoActivity::class.java)
            goIntent.putExtra("USERID", viewModel.userId.value!!.toLong())
            goIntent.putExtra("ISEDIT", true)
            goIntent.putExtra("AGE", viewModel.userData.value!!.age)
            goIntent.putExtra("SEX", viewModel.userData.value!!.sex)
            goIntent.putExtra("HEIGHT", viewModel.userData.value!!.height)
            goIntent.putExtra("WEIGHT", viewModel.userData.value!!.weight)
            goIntent.putExtra("DIABETE", viewModel.userData.value!!.diabeteCode)
            activityResultLauncher.launch(goIntent)
        }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result : ActivityResult ->
            val resultIntent = result.data
            if (resultIntent != null) {
                if (result.resultCode == R.integer.join_success) {
                    viewModel.getUserId()
                }
            }
        }

        return binding.root
    }
}