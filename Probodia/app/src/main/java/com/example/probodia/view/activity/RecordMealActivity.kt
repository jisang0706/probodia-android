package com.example.probodia.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.example.probodia.BuildConfig
import com.example.probodia.R
import com.example.probodia.adapter.FoodAddAdapter
import com.example.probodia.data.remote.body.PostMealBody
import com.example.probodia.databinding.ActivityRecordMealBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.view.fragment.RecordFragment
import com.example.probodia.view.fragment.TimeSelectorFragment
import com.example.probodia.viewmodel.RecordAnythingViewModel
import com.example.probodia.viewmodel.RecordMealViewModel
import com.example.probodia.viewmodel.factory.RecordAnythingViewModelFactory
import com.example.probodia.viewmodel.factory.RecordMealViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class RecordMealActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecordMealBinding

    private lateinit var mealViewModel : RecordMealViewModel
    private lateinit var mealViewModelFactory : RecordMealViewModelFactory

    private lateinit var baseViewModel : RecordAnythingViewModel
    private lateinit var baseViewModelFactory : RecordAnythingViewModelFactory

    private lateinit var listAdapter : FoodAddAdapter

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(
            applicationContext, R.color.alpha_30
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_meal)

        mealViewModelFactory = RecordMealViewModelFactory(PreferenceRepository(applicationContext))
        mealViewModel = ViewModelProvider(this, mealViewModelFactory).get(RecordMealViewModel::class.java)
        binding.mealVm = mealViewModel

        baseViewModelFactory = RecordAnythingViewModelFactory(4)
        baseViewModel = ViewModelProvider(this, baseViewModelFactory).get(RecordAnythingViewModel::class.java)
        binding.baseVm = baseViewModel

        binding.lifecycleOwner = this

        listAdapter = FoodAddAdapter()
        binding.foodAddRv.adapter = listAdapter
        binding.foodAddRv.layoutManager = LinearLayoutManager(applicationContext)

        listAdapter.setOnItemButtonClickListener(object : FoodAddAdapter.OnItemButtonClickListener {
            override fun onItemDeleteClick(position: Int) {
                listAdapter.deleteItem(position)
                listAdapter.notifyDataSetChanged()
                baseViewModel.setButtonClickEnable(listAdapter.itemCount > 0)
            }

            override fun onItemEditClick(position: Int) {
                Log.e("FOOD CLICKED", "EDIT")
            }

        })

        binding.searchBtn.setOnClickListener {
            val intent = Intent(applicationContext, SearchFoodActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        binding.cameraBtn.setOnClickListener {
            checkCameraPermission()
        }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result : ActivityResult ->
            when (result.resultCode) {
                R.integer.record_meal_add_code -> {
                    val intent = result.data
                    if (intent != null) {
                        val addFood: PostMealBody.PostMealItem = intent!!.getParcelableExtra("ADDFOOD")!!
                        listAdapter.addItem(addFood)
                        listAdapter.notifyDataSetChanged()
                        baseViewModel.setButtonClickEnable(listAdapter.itemCount > 0)
                    }
                }
                RESULT_OK -> {
                    val bitmap = result.data?.extras?.get("data") as Bitmap
                    mealViewModel.setFoodImage(bitmap)
                    uploadImage(bitmap)
                }
            }
        }

        initTimeSelector()

        binding.enterBtn.setOnClickListener {
            if (baseViewModel.buttonClickEnable.value!!) {
                mealViewModel.postMeal(
                    when(baseViewModel.selectedTimeTag.value) {
                        1 -> "아침"
                        2 -> "점심"
                        3 -> "저녁"
                        else -> "아침"
                    },
                    listAdapter.getList(),
                    baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
            } else {
                Toast.makeText(applicationContext, "입력된 식단이 없습니다.", Toast.LENGTH_LONG).show()
            }
        }

        mealViewModel.mealResult.observe(this, Observer {
            val resultIntent = Intent(applicationContext, RecordFragment::class.java)
            resultIntent.putExtra("RELOAD", true)
            setResult(R.integer.record_meal_result_code, resultIntent)
            finish()
        })

        baseViewModel.buttonClickEnable.observe(this, Observer {
            if (it) {
                binding.enterBtn.setBackgroundResource(R.drawable.primary_100_2_background)
            } else {
                binding.enterBtn.setBackgroundResource(R.drawable.gray_300_2_background)
            }
        })

        mealViewModel.foodNamesResult.observe(this, Observer {
            val intent = Intent(applicationContext, RecognitionFoodActivity::class.java)
            intent.putExtra("foodNames", it.foodList.toTypedArray())
            intent.putExtra("foodImage", mealViewModel.foodImage.value)
            activityResultLauncher.launch(intent)
        })

        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }

    fun initTimeSelector() {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = TimeSelectorFragment()
        transaction.replace(R.id.time_selector_frame, fragment)
        transaction.commit()
    }

    fun checkCameraPermission() {
        val cameraPermission = ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.CAMERA
        )

        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResultLauncher.launch(intent)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), R.integer.permission_camera)
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

        val awsCredentials = BasicAWSCredentials(BuildConfig.AWS_S3_ACCESS_KEY, BuildConfig.AWS_S3_SECRET_KEY)
        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2))

        val transferUtility = TransferUtility.builder().s3Client(s3Client).context(applicationContext).build()
        TransferNetworkLossHandler.getInstance(applicationContext)

        val uploadObserver = transferUtility.upload("probodia-food-s3-bucket/food", file.name, file)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    CoroutineScope(Dispatchers.IO).launch {
                        mealViewModel.getImageFood(file.name)
                        contentResolver.delete(uri, null, null)
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
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, fileName, null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(contentUri : Uri) : String? {
        var cursor : Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            cursor!!.getString(column_index)
        } finally {
            cursor?.close()
        }
    }
}