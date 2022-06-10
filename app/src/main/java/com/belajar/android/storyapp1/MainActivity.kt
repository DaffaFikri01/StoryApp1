package com.belajar.android.storyapp1

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.belajar.android.storyapp1.Api.ApiConfig
import com.belajar.android.storyapp1.Login.LoginResponse
import com.belajar.android.storyapp1.Login.LoginVM
import com.belajar.android.storyapp1.Model.UserModelData
import com.belajar.android.storyapp1.Preference.UserPreferences
import com.belajar.android.storyapp1.Register.RegisterActivity
import com.belajar.android.storyapp1.Story.AllUserStoryActivity
import com.belajar.android.storyapp1.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var loginVM : LoginVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAnimation()
        setupLogVM()
        toRegisterActivity()

        binding.apply {
            btnLogin.setOnClickListener {
                setupAction()
            }
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

    private fun toRegisterActivity() {
        binding.btnRegister1.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupLogVM() {
        loginVM = ViewModelProvider(this,
            VMFactory(UserPreferences.getInstance(dataStore)))[LoginVM::class.java]

        loginVM.loginUsers().observe(this) { users ->
            if (users.isLogin){
                startActivity(Intent(this, AllUserStoryActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        val logEmail = binding.etLoginEmail.text.toString()
        val logPass = binding.etLoginPassword.text.toString()

        if (binding.etLoginEmail.length() == 0 && binding.etLoginPassword.length() == 0){
            binding.etLoginEmail.error = resources.getString(R.string.email_warn_msg)
            binding.etLoginPassword.error = resources.getString(R.string.password_warn_msg)
        }
        else if (binding.etLoginEmail.length() != 0 && binding.etLoginPassword.length() >= 6) {
            showLoading(true)

            ApiConfig.getApiService().login(logEmail, logPass)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            response.body()?.loginResult?.apply {
                                saveSession(token)
                            }
                            Toast.makeText(
                                this@MainActivity,
                                resources.getString(R.string.loginSuccessful),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(this@MainActivity, resources.getString(R.string.loginFail), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, resources.getString(R.string.loginFail), Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    private fun saveSession(token : String?){
        loginVM.sessionSave(UserModelData(token.toString(), true))
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}