package com.belajar.android.storyapp1.Story

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.belajar.android.storyapp1.Api.ApiConfig
import com.belajar.android.storyapp1.Preference.UserPreferences
import com.belajar.android.storyapp1.R
import com.belajar.android.storyapp1.Utils.createTempFile
import com.belajar.android.storyapp1.Utils.reduceFileImage
import com.belajar.android.storyapp1.Utils.rotateBitmap
import com.belajar.android.storyapp1.Utils.uriToFile
import com.belajar.android.storyapp1.VMFactory
import com.belajar.android.storyapp1.databinding.ActivityAddStoryBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private lateinit var addStoryVM: AllStoryVM
    private var getFile : File? = null

    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (!allPermissionsGranted()){
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Add Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        if (!allPermissionsGranted()){
            ActivityCompat.requestPermissions(
                this@AddStoryActivity,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        addStoryVM = ViewModelProvider(
            this,
            VMFactory(UserPreferences.getInstance(dataStore))
        )[AllStoryVM::class.java]

        binding.apply {
            btnGallery.setOnClickListener {
                openGallery()
            }

            btnCamera.setOnClickListener {
                startTakePhoto()
            }

            btnUpload.setOnClickListener {
                uploadPicture()
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.belajar.android.storyapp1.Story",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val myFile = File (currentPhotoPath)
            getFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                true
            )

            binding.imgAddPreview.setImageBitmap(result)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choosePicture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myPicture = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myPicture
            binding.imgAddPreview.setImageURI(selectedImg)
        }
    }

    private fun uploadPicture() {
        val desc = binding.etDescriptionStory.text.toString()

        when {
            desc.isEmpty() -> {
                binding.etDescriptionStory.error = resources.getString(R.string.desc_error_msg)
            }
            else -> {
                showLoading(true)
                if (getFile != null){
                    val file = reduceFileImage(getFile as File)
                    val description = binding.etDescriptionStory.text.toString().toRequestBody("text/plain".toMediaType())
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )

                    addStoryVM.getUser().observe(this){ add ->
                        if (add != null){
                            ApiConfig.getApiService().postStory("Bearer " + add.token, imageMultipart, description)
                                .enqueue(object : Callback<AddNewStoryResponse>{
                                    override fun onResponse(
                                        call: Call<AddNewStoryResponse>,
                                        response: Response<AddNewStoryResponse>
                                    ) {
                                        showLoading(false)
                                        if (response.isSuccessful){
                                            Toast.makeText(
                                                this@AddStoryActivity,
                                                resources.getString(R.string.uploadSuccessful),
                                                Toast.LENGTH_SHORT).show()
                                            movetoAllUserStory()
                                        }
                                        else {
                                            Toast.makeText(
                                                this@AddStoryActivity,
                                                resources.getString(R.string.uploadFail),
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<AddNewStoryResponse>, t: Throwable) {
                                        Toast.makeText(
                                            this@AddStoryActivity,
                                            resources.getString(R.string.uploadFail),
                                            Toast.LENGTH_SHORT).show()
                                    }
                                })
                        }
                    }
                }
            }
        }
    }

    private fun uploadImg(){
    }

    private fun movetoAllUserStory() {
        startActivity(Intent(this, AllUserStoryActivity::class.java))
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}