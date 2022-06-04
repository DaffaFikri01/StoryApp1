package com.belajar.android.storyapp1.Story

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.belajar.android.storyapp1.Utils.uriToFile
import com.belajar.android.storyapp1.databinding.ActivityAddStoryBinding
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddStoryBinding
    private var getFile : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Add Story"

        binding.apply {
            btnGallery.setOnClickListener {
                openGallery()
            }

            btnCamera.setOnClickListener {
                openCamera()
            }

            btnUpload.setOnClickListener {
                uploadPicture()
            }
        }
    }

    private fun openCamera() {
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myPicture = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myPicture
            binding.imgAddPreview.setImageURI(selectedImg)
        }
    }

    private fun uploadPicture() {
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}