package com.example.mystoryapp.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityAddStoryBinding
import com.example.mystoryapp.helper.createCustomTempFile
import com.example.mystoryapp.helper.reduceFileImage
import com.example.mystoryapp.helper.uriToFile
import com.example.mystoryapp.viewmodel.AddStoryViewModel
import com.example.mystoryapp.viewmodel.LoginPreference
import com.example.mystoryapp.viewmodel.UserPreference
import com.example.mystoryapp.viewmodel.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private val addStoryViewModel by viewModels<AddStoryViewModel>()
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private lateinit var token: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.title_add_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pref = UserPreference.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this@AddStoryActivity, ViewModelFactory(pref))[LoginPreference::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        loginViewModel.getToken().observe(this) {
            token = it
        }

        addStoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnAddCamera.setOnClickListener {
            startTakePhoto()
        }

        binding.btnAddGallery.setOnClickListener {
            startGallery()
        }

        binding.buttonAdd.setOnClickListener {
            uploadImage()
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            myFile.let { file ->
                getFile = file
                binding.ivStoryAdd.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).let { file ->
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                getString(R.string.package_name),
                file
            )
            currentPhotoPath = file.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            binding.ivStoryAdd.setImageURI(selectedImg)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, R.string.choose_picture.toString())
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        val desc = binding.edAddDescription.text.toString()
        if (getFile == null) {
            Toast.makeText(this@AddStoryActivity, R.string.insert_picture, Toast.LENGTH_SHORT).show()
        } else if (desc == "") {
            Toast.makeText(this@AddStoryActivity, R.string.insert_desc, Toast.LENGTH_SHORT).show()
        } else {
            val file = reduceFileImage(getFile as File)
            val desc = desc.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            addStoryViewModel.upload(imageMultipart, desc, token)
            addStoryViewModel.message.observe(this) {
                when(it) {
                    "Story created successfully" -> {
                        Toast.makeText(this, R.string.story_success_added, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else -> {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbarAddstory.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}