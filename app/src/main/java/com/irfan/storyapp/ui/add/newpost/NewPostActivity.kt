package com.irfan.storyapp.ui.add.newpost

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.irfan.storyapp.R
import com.irfan.storyapp.data.model.remote.story.add.NewStoryResponse
import com.irfan.storyapp.data.network.ApiConfig
import com.irfan.storyapp.databinding.ActivityNewPostBinding
import com.irfan.storyapp.utils.Util
import com.irfan.storyapp.utils.Util.reduceFileImage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@ExperimentalPagingApi
class NewPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPostBinding

    private lateinit var currentPath: String

    private var getFile: File? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val newPostViewModel: NewPostViewModel by viewModels()

    private var token: String = ""

    private var location: Location? = null

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Add Story"
            show()
        }
        initView()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        lifecycleScope.launchWhenCreated {
            launch {
                newPostViewModel.getUserToken().collect() { userToken ->
                    if (!userToken.isNullOrEmpty()) token = userToken
                }
            }
        }
        playAnimation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(this, "Permission is not granted", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun initView() {
        binding.apply {
            cameraAddBtn.setOnClickListener { startCamera() }
            galleryAddBtn.setOnClickListener { startGallery() }
            postAddBtn.setOnClickListener { addStory() }
            smLocation.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    getLastestLocation()
                } else {
                    location = null
                }
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        Util.createTempFile(application).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                this,
                "com.irfan.storyapp",
                it
            )
            currentPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            cameraIntentLauncher.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }

        val chooser = Intent.createChooser(intent, "Choose Image")
        galleryIntentLauncher.launch(chooser)
    }

    private fun getLastestLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Location permission granted
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    this.location = location
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.activate_location_msg),
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.smLocation.isChecked = false
                }
            }
        } else {
            // Location permission denied
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val cameraIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPath)
            getFile = myFile

            Glide.with(this)
                .load(getFile)
                .centerCrop()
                .into(binding.imageAddIv)
        }
    }

    private val galleryIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = Util.uriToFile(selectedImg, this)

            getFile = myFile
            Glide.with(this)
                .load(getFile)
                .centerCrop()
                .into(binding.imageAddIv)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getLastestLocation()
            }
            else -> {
                Toast.makeText(
                    this,
                    getString(R.string.activate_location_msg),
                    Toast.LENGTH_SHORT
                ).show()

                binding.smLocation.isChecked = false
            }
        }
    }

    private fun addStory() {

        if (binding.descAddTiet.toString().isBlank()) {
            binding.descAddTiet.error = getString(R.string.alert_empty_field_desc)
            return
        }

        if (getFile != null) {
            lifecycleScope.launchWhenCreated {
                val file = reduceFileImage(getFile as File)
                val descriptionText = binding.descAddTiet.text.toString()
                val description = descriptionText.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                var lon: RequestBody? = null
                var lat: RequestBody? = null

                if (location != null) {
                    lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
                    lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                }

                newPostViewModel.uploadNewStory(token, imageMultipart, description, lat, lon).collect { result ->
                    result.onSuccess { response ->
                        if (!response.error) {
                            Toast.makeText(
                                this@NewPostActivity,
                                response.message,
                                Toast.LENGTH_LONG
                            ).show()
                            onBackPressed()
                        } else {
                            Toast.makeText(
                                this@NewPostActivity,
                                response.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    result.onFailure { t ->
                        Toast.makeText(this@NewPostActivity, t.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                }

            }
        } else {
            Toast.makeText(this, "Please input a picture first", Toast.LENGTH_LONG).show()
            return
        }

    }

    private fun playAnimation() {

        val imageIv = ObjectAnimator.ofFloat(binding.imageAddIv, View.ALPHA, 1f).setDuration(500)
        val cameraBtn =
            ObjectAnimator.ofFloat(binding.cameraAddBtn, View.ALPHA, 1f).setDuration(200)
        val galleryBtn =
            ObjectAnimator.ofFloat(binding.galleryAddBtn, View.ALPHA, 1f).setDuration(500)
        val descTil = ObjectAnimator.ofFloat(binding.descAddTil, View.ALPHA, 1f).setDuration(500)
        val postBtn = ObjectAnimator.ofFloat(binding.postAddBtn, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                imageIv,
                cameraBtn,
                galleryBtn,
                descTil,
                postBtn
            )
            startDelay = 500
        }.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}