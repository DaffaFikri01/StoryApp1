package com.belajar.android.storyapp1.Register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.belajar.android.storyapp1.Api.ApiConfig
import com.belajar.android.storyapp1.MainActivity
import com.belajar.android.storyapp1.R
import com.belajar.android.storyapp1.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Register"

        setAnimation()

        binding.apply {
            btnRegister2.setOnClickListener {
                registerClick()
            }
        }
    }

    private fun setAnimation() {
        val regisTitle = ObjectAnimator.ofFloat(binding.regisTitle, View.ALPHA, 1f).setDuration(500)
        val btnRegisName = ObjectAnimator.ofFloat(binding.tilName, View.ALPHA, 1f).setDuration(500)
        val btnRegisEmail = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(500)
        val btnRegisPassword = ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(500)
        val btnRegis2 = ObjectAnimator.ofFloat(binding.btnRegister2, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(regisTitle, btnRegisName, btnRegisEmail, btnRegisPassword, btnRegis2)
            start()
        }
    }

    private fun registerClick() {
        val regisName = binding.etRegisName.text.toString()
        val regisEmail = binding.etRegisEmail.text.toString()
        val regisPassword = binding.etRegisPassword.text.toString()

        if (binding.etRegisName.length() == 0 && binding.etRegisEmail.length() == 0 && binding.etRegisPassword.length() == 0){
            binding.etRegisName.error = resources.getString(R.string.name_warn_msg)
            binding.etRegisEmail.error = resources.getString(R.string.email_warn_msg)
            binding.etRegisPassword.error = resources.getString(R.string.password_warn_msg)

        }else if (binding.etRegisName.length() != 0 && binding.etRegisEmail.length() != 0 && binding.etRegisPassword.length() >= 6){
            showLoading(true)

            ApiConfig.getApiService().register(regisName, regisEmail, regisPassword)
                .enqueue(object : Callback<RegisterResponse>{
                    override fun onResponse(call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        showLoading(false)

                        if (response.isSuccessful){
                            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                            finish()
                            Toast.makeText(this@RegisterActivity,
                                resources.getString(R.string.registerSuccessful), Toast.LENGTH_SHORT).show()
                            showLoading(true)
                        }
                        else {
                            Toast.makeText(this@RegisterActivity,
                                resources.getString(R.string.registerFail), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@RegisterActivity, resources.getString(R.string.registerFail), Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}