package com.belajar.android.storyapp1.Register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.belajar.android.storyapp1.R
import com.belajar.android.storyapp1.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Register"

        binding.apply {
            btnRegister2.setOnClickListener {
                registerClick()
            }
        }
    }

    private fun registerClick() {
        val regisName = binding.etRegisName.text.toString()
        val regisEmail = binding.etRegisEmail.text.toString()
        val regisPassword = binding.etRegisPassword.text.toString()

        when {
            regisName.isEmpty() -> {
                binding.etRegisName.error = resources.getString(R.string.name_error_msg)
            }

            regisEmail.isEmpty() -> {
                binding.etRegisEmail.error = resources.getString(R.string.email_warn_msg)
            }

            regisPassword.isEmpty() -> {
                binding.etRegisPassword.error = resources.getString(R.string.password_warn_msg)
            }
            else -> {

            }
        }
    }
}