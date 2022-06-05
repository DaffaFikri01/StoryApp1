package com.belajar.android.storyapp1

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.belajar.android.storyapp1.Register.RegisterActivity
import com.belajar.android.storyapp1.Story.AllUserStoryActivity
import com.belajar.android.storyapp1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAnimation()

        binding.apply {
            btnRegister1.setOnClickListener{
                toRegisterActivity()
            }

            btnLogin.setOnClickListener {
                toAllUserStoryActivity()
            }
        }
    }


    private fun toRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun toAllUserStoryActivity() {
        if (binding.etLoginEmail.length() == 0 && binding.etLoginPassword.length() == 0){
            binding.etLoginEmail.error = resources.getString(R.string.email_warn_msg)
            binding.etLoginPassword.error = resources.getString(R.string.password_warn_msg)
        }else if (binding.etLoginEmail.length() != 0 && binding.etLoginPassword.length() >= 6){
            val intent = Intent(this, AllUserStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setAnimation() {
        val title = ObjectAnimator.ofFloat(binding.storyApp, View.ALPHA, 1f).setDuration(500)
        val subtitle = ObjectAnimator.ofFloat(binding.subtitle, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val btnRegister1 = ObjectAnimator.ofFloat(binding.btnRegister1, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, subtitle, btnLogin, btnRegister1)
            start()
        }
    }
}