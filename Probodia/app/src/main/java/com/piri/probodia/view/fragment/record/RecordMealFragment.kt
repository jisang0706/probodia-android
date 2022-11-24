package com.piri.probodia.view.fragment.record

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.piri.probodia.BuildConfig
import com.piri.probodia.R
import com.piri.probodia.adapter.FoodAddAdapter
import com.piri.probodia.data.remote.body.PostMealBody
import com.piri.probodia.data.remote.model.MealDto
import com.piri.probodia.databinding.FragmentRecordMealBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.RecordAnythingViewModel
import com.piri.probodia.viewmodel.RecordMealViewModel
import com.piri.probodia.viewmodel.factory.RecordAnythingViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.piri.probodia.data.remote.body.FoodAllGLBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.random.Random

class RecordMealFragment(val reload : () -> Unit, val recordType : Int, val data : MealDto.Record?) : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentRecordMealBinding

    private lateinit var mealViewModel : RecordMealViewModel

    private lateinit var baseViewModel : RecordAnythingViewModel
    private lateinit var baseViewModelFactory : RecordAnythingViewModelFactory

    private lateinit var listAdapter : FoodAddAdapter

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initTimeSelector()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_meal, container, false)

        mealViewModel = ViewModelProvider(this).get(RecordMealViewModel::class.java)
        binding.mealVm = mealViewModel

        baseViewModelFactory = RecordAnythingViewModelFactory(4)
        baseViewModel = ViewModelProvider(this, baseViewModelFactory).get(RecordAnythingViewModel::class.java)
        binding.baseVm = baseViewModel

        binding.lifecycleOwner = this

        listAdapter = FoodAddAdapter()
        binding.foodAddRv.adapter = listAdapter
        binding.foodAddRv.layoutManager = LinearLayoutManager(requireContext())

        listAdapter.setOnItemButtonClickListener(object  : FoodAddAdapter.OnItemButtonClickListener {
            override fun onItemDeleteClick(position: Int) {
                listAdapter.deleteItem(position)
                listAdapter.notifyDataSetChanged()
                baseViewModel.setInputAll(listAdapter.itemCount > 0)
                getFoodAllGL()
            }

            override fun onItemEditClick(position: Int) {
                Log.e("FOOD CLICKED", "EDIT")
            }

        })

        setRecordBaseTime()

        if (recordType == 1) {
            for(i in 0 until data!!.mealDetails.size) {
                listAdapter.addItem(
                    PostMealBody.PostMealItem(
                    data.mealDetails[i].foodName,
                    data.mealDetails[i].foodId,
                    data.mealDetails[i].quantity.toDouble(),
                    data.mealDetails[i].calories,
                    data.mealDetails[i].bloodSugar,
                    data.mealDetails[i].imageUrl
                ))
            }
            listAdapter.notifyDataSetChanged()
            baseViewModel.setInputAll(listAdapter.itemCount > 0)
            getFoodAllGL()
        }

        binding.searchBtn.setOnClickListener {
            val fragment = SearchFoodFragment(R.integer.record, ::addMealItem)
            fragment.show(childFragmentManager, fragment.tag)
        }

        binding.cameraBtn.setOnClickListener {
            checkCameraPermission()
        }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result : ActivityResult ->
            when (result.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val bitmap = result.data?.extras?.get("data") as Bitmap
                    mealViewModel.setFoodImage(bitmap)
                    uploadImage(bitmap)
                }
            }
        }

        binding.enterBtn.setOnClickListener {
            if (baseViewModel.buttonClickEnable.value!!) {
                baseViewModel.setServerFinish(false)
                if (recordType == 1) {
                    mealViewModel.putMeal(
                        PreferenceRepository(requireContext()),
                        data!!.recordId,
                        getSelectedTimeTag(),
                        listAdapter.getList(),
                        baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                } else {
                    mealViewModel.postMeal(
                        PreferenceRepository(requireContext()),
                        getSelectedTimeTag(),
                        listAdapter.getList(),
                        baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                }
            } else {
                Toast.makeText(requireContext(), "입력된 식단이 없습니다.", Toast.LENGTH_LONG).show()
            }
        }

        mealViewModel.mealResult.observe(this, {
            baseViewModel.setServerFinish(true)
            reload()
            parentFragmentManager.beginTransaction().remove(this).commit()
        })

        baseViewModel.buttonClickEnable.observe(this, {
            if (it) {
                binding.enterBtn.setBackgroundResource(R.drawable.primary_100_2_background)
            } else {
                binding.enterBtn.setBackgroundResource(R.drawable.gray_300_2_background)
            }
        })

        mealViewModel.foodNamesResult.observe(this, {
            val fragment = RecognitionFoodFragment(::addMealItem, mealViewModel.foodImage.value, it.foodList)
            fragment.show(childFragmentManager, fragment.tag)
        })

        mealViewModel.foodGL.observe(this) {
            Log.e("GLUCOSE", it.foodGL.toString())
            binding.glucoseText.text = when(it.healthGL) {
                "high" -> "조금만 더 열심히 관리해보아요"
                "mid" -> "잘하고있어요"
                "low" -> "와우! 최고의 식단인걸요"
                else -> "와우! 최고의 식단인걸요"
            }

            binding.glucoseIcon.setImageResource(
                when(it.healthGL) {
                    "high" -> R.drawable.sad
                    "mid" -> R.drawable.soso
                    "low" -> R.drawable.smile
                    else -> R.drawable.smile
                }
            )
        }

        binding.cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        mealViewModel.isError.observe(this) {
            baseViewModel.setServerFinish(true)
            Toast.makeText(requireContext(), "인터넷 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
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

    fun initTimeSelector() {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = TimeSelectorFragment()
        transaction.replace(R.id.time_selector_frame, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun setRecordBaseTime() {
        baseViewModel.setSelectedTimeTag(when(data!!.timeTag) {
            "아침" -> 1
            "점심" -> 2
            "저녁" -> 3
            else -> 1
        })
        val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        baseViewModel.setLocalDateTime(LocalDateTime.parse(data.recordDate, localDateTimeFormatter))
    }

    fun checkCameraPermission() {
        val cameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        )

        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResultLauncher.launch(intent)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), R.integer.permission_camera)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == R.integer.permission_camera) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                activityResultLauncher.launch(intent)
            }
        }
    }

    fun uploadImage(bitmap : Bitmap) {
        val uri = bitmapToUri("${Random.nextInt(0, 999999)}_${System.currentTimeMillis()}", bitmap)
        val file = File(getRealPathFromURI(uri))

        val credentialProvider = CognitoCachingCredentialsProvider(
            requireContext(),
            BuildConfig.AWS_COGNITO_POOL_ID,
            Regions.AP_NORTHEAST_2
        )
        val s3Client = AmazonS3Client(credentialProvider, Region.getRegion(Regions.AP_NORTHEAST_2))

        TransferNetworkLossHandler.getInstance(requireContext())

        val transferUtility = TransferUtility.builder()
            .context(requireContext())
            .s3Client(s3Client)
            .build()

        val uploadObserver = transferUtility.upload("probodia-food-s3-bucket/food", file.name, file)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    CoroutineScope(Dispatchers.IO).launch {
                        mealViewModel.getImageFood(PreferenceRepository(requireContext()), file.name)
                        requireActivity().contentResolver.delete(uri, null, null)
                    }

                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                val done = (bytesCurrent.toDouble() / bytesTotal * 100.0).toInt()
                Log.e("AWS", "UPLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: Exception?) {
                Log.e("AWS", "UPLOAD ERROR - - ID: $id - - EX:$ex")
            }

        })
    }

    fun bitmapToUri(fileName : String, bitmap : Bitmap) : Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(requireActivity().contentResolver, bitmap, fileName, null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(contentUri : Uri) : String? {
        var cursor : Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = requireActivity().contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            cursor!!.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    fun getSelectedTimeTag() : String {
        return when(baseViewModel.selectedTimeTag.value) {
            1 -> "아침"
            2 -> "점심"
            3 -> "저녁"
            else -> "아침"
        }
    }

    fun addMealItem(meal : PostMealBody.PostMealItem) {
        meal.quantity = meal.quantity.roundToInt().toDouble()
        listAdapter.addItem(meal)
        listAdapter.notifyDataSetChanged()
        getFoodAllGL()
        baseViewModel.setInputAll(listAdapter.itemCount > 0)
    }

    fun getFoodAllGL() {
        if (listAdapter.itemCount > 0) {
            binding.glucoseTopLayout.visibility = View.VISIBLE
            binding.glucoseLayout.visibility = View.VISIBLE
            val foodAllGLBody = FoodAllGLBody(
                buildList {
                    for (item in listAdapter.getList()) {
                        add(
                            FoodAllGLBody.FoodGL(
                                item.foodId,
                                item.quantity.roundToInt()
                            )
                        )
                    }
                }.toMutableList()
            )

            mealViewModel.getFoodGL(PreferenceRepository(requireContext()), foodAllGLBody)
        } else {
            binding.glucoseTopLayout.visibility = View.GONE
            binding.glucoseLayout.visibility = View.GONE
        }
    }
}