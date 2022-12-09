package com.piri.probodia.view.fragment.record

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.piri.probodia.R
import com.piri.probodia.adapter.FoodInfoAdapter
import com.piri.probodia.data.remote.body.PostMealBody
import com.piri.probodia.data.remote.model.FoodDetailDto
import com.piri.probodia.databinding.FragmentSearchFoodDetailBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.SearchFoodDetailViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.piri.probodia.data.remote.body.FoodGLBody
import com.piri.probodia.view.fragment.FoodGLFragment
import java.lang.Integer.min
import kotlin.math.roundToInt

class SearchFoodDetailFragment(val kind : Int, val foodId : String, val applyItem : (item : PostMealBody.PostMealItem) -> Unit) : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentSearchFoodDetailBinding

    private lateinit var viewModel : SearchFoodDetailViewModel

    private lateinit var listAdapter : FoodInfoAdapter

    private var first = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_food_detail, container, false)

        binding.lifecycleOwner = this

        viewModel = SearchFoodDetailViewModel()
        binding.vm = viewModel

        if (kind == R.integer.search) {
            binding.enterBtn.text = "닫기"
        }

        viewModel.getFoodInfo(PreferenceRepository(requireContext()), foodId)

        viewModel.foodInfo.observe(this, {
            binding.foodNameText.text = it.name

            listAdapter = FoodInfoAdapter(getFoodInfoList(it))
            binding.foodInfoRv.adapter = listAdapter
            binding.foodInfoRv.layoutManager = LinearLayoutManager(requireContext())

            getFoodGL(it, it.quantityByOne)
        })

        viewModel.foodGL.observe(this) {
            Log.e("FOODDETAIL", it.toString())
            binding.glucoseText.text = when(it.main.healthGL) {
                "high" -> "조금만 더 열심히 관리해보아요"
                "mid" -> "잘하고있어요"
                "low" -> "와우! 최고의 식단인걸요"
                else -> "와우! 최고의 식단인걸요"
            }

            binding.glucoseIcon.setImageResource(
                when (it.main.healthGL) {
                    "high" -> R.drawable.sad
                    "mid" -> R.drawable.soso
                    "low" -> R.drawable.smile
                    else -> R.drawable.smile
                }
            )

            if (first) {
                first = false
                viewModel.getFoodGLInfo(
                    PreferenceRepository(requireContext()),
                    it.foodBig.foodId,
                    it.foodSmall.foodId
                )
            }
        }

        binding.quantityEdit.addTextChangedListener { edittext ->
            if (binding.quantityEdit.length() > 0 && "${binding.quantityEdit.text}"[0] == '.') {
                binding.quantityEdit.setText("0" + "${binding.quantityEdit.text}")
                binding.quantityEdit.setSelection(binding.quantityEdit.text.length)
            }

            if (edittext!!.count { it == '.' } > 1) {
                val ind = "${binding.quantityEdit.text!!}".indexOfLast { it == '.' }
                binding.quantityEdit.setText(
                    "${binding.quantityEdit.text}".substring(0, ind) +
                            "${binding.quantityEdit.text}".substring(ind + 1, binding.quantityEdit.text.length)
                )
                binding.quantityEdit.setSelection(binding.quantityEdit.text.length)
            }

            if (edittext!!.isNotEmpty() && "${binding.quantityEdit.text!!}".last() != '.') {
                getFoodGL(
                    viewModel.foodInfo.value!!,
                    getQuantity()
                )
            }
        }

        binding.quantityBtn.setOnClickListener {
            if (binding.quantityBtn.text == "인분") {
                binding.quantityBtn.setText("g")
                binding.quantityEdit.setText(("${binding.quantityEdit.text}".toDouble() * viewModel.foodInfo.value!!.quantityByOne).toInt().toString())
            } else {
                binding.quantityBtn.text = "인분"
                var personQuantity = ("${binding.quantityEdit.text}".toDouble() / viewModel.foodInfo.value!!.quantityByOne)
                val personQuantityText = if ((personQuantity * 1000).toInt() == personQuantity.toInt() * 1000) {
                    "${personQuantity.toInt()}"
                } else {
                    "$personQuantity"
                }
                binding.quantityEdit.setText(personQuantityText.substring(0, min(4, personQuantityText.length)))
            }
        }

        binding.quantityText.setOnClickListener {
            val drawable : Drawable?
            if (binding.foodInfoRv.visibility == View.GONE) {
                binding.foodInfoRv.visibility = View.VISIBLE
                binding.foodGlLayout.visibility = View.GONE
                drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_uparrow)
            } else {
                binding.foodInfoRv.visibility = View.GONE
                binding.foodGlLayout.visibility = View.VISIBLE
                drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_downarrow)
            }
            binding.quantityText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        }

        binding.enterBtn.setOnClickListener {
            if (kind == R.integer.record) {
                if ("${binding.quantityEdit.text}" == "") {
                    binding.quantityEdit.setText("1")
                    binding.quantityBtn.text = "인분"
                }
                if ("${binding.quantityEdit.text}"[binding.quantityEdit.text.length - 1] == '.') {
                    binding.quantityEdit.setText(
                        "${binding.quantityEdit.text}".substring(
                            0,
                            "${binding.quantityEdit.text}".length - 1
                        )
                    )
                }
                val postMealItem = PostMealBody.PostMealItem(
                    viewModel.foodInfo.value!!.name,
                    foodId,
                    getQuantity().toDouble(),
                    viewModel.foodInfo.value!!.calories.roundToInt(),
                    0,
                    ""
                )
                applyItem(postMealItem)
            }
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        viewModel.foodBigInfo.observe(viewLifecycleOwner) {
            if (it.name.isEmpty()) {
                binding.foodBig.visibility = View.GONE
                if (binding.foodSmall.visibility == View.GONE) {
                    binding.foodGlLayout.visibility = View.GONE
                }
            }
        }

        viewModel.foodSmallInfo.observe(viewLifecycleOwner) {
            if (it.name.isEmpty()) {
                binding.foodSmall.visibility = View.GONE
                if (binding.foodBig.visibility == View.GONE) {
                    binding.foodGlLayout.visibility = View.GONE
                }
            }
        }

        binding.foodBig.setOnClickListener {
            val fragment = FoodGLFragment(
                viewModel.foodGL.value!!.foodBig.foodId,
                viewModel.foodBigInfo.value!!
            )
            fragment.show(childFragmentManager, fragment.tag)
        }

        binding.foodSmall.setOnClickListener {
            val fragment = FoodGLFragment(
                viewModel.foodGL.value!!.foodSmall.foodId,
                viewModel.foodSmallInfo.value!!
            )
            fragment.show(childFragmentManager, fragment.tag)
        }

        viewModel.isGLError.observe(viewLifecycleOwner) {
            binding.glucoseLayout.visibility = View.GONE
        }

        binding.cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog : Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setUpRatio(bottomSheetDialog, 80)
        }
        return dialog
    }

    fun getFoodInfoList(item : FoodDetailDto) : MutableList<Pair<String, Double>> {
        return mutableListOf(
            Pair("탄수화물", item.carbohydrate),
            Pair("당류", item.sugars),
            Pair("단백질", item.protein),
            Pair("지방", item.fat),
            Pair("콜레스테롤", item.cholesterol),
            Pair("나트륨", item.salt)
        )
    }

    fun getFoodGL(item : FoodDetailDto, quantity : Int) {
        val ratio = quantity / item.quantityByOne
        viewModel.getFoodGL(PreferenceRepository(requireContext()), FoodGLBody(
            foodId,
            item.bigCategory,
            item.smallCategory,
            item.name,
            quantity,
            item.quantityByOneUnit,
            item.calories * ratio,
            item.carbohydrate * ratio,
            item.sugars * ratio,
            item.protein * ratio,
            item.fat * ratio,
            item.transFat * ratio,
            item.saturatedFat * ratio,
            item.cholesterol * ratio,
            item.salt * ratio
        ))
    }

    fun getQuantity() : Int {
        return if (binding.quantityBtn.text == "인분") {
            ("${binding.quantityEdit.text!!}".toDouble() *
                    viewModel.foodInfo.value!!.quantityByOne).toInt()
        } else {
            "${binding.quantityEdit.text!!}".toDouble().roundToInt()
        }
    }
}