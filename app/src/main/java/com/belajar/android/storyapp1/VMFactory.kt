package com.belajar.android.storyapp1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.belajar.android.storyapp1.Login.LoginVM
import com.belajar.android.storyapp1.LogoutVM.LogoutVM
import com.belajar.android.storyapp1.Preference.UserPreferences
import com.belajar.android.storyapp1.Register.RegisterViewModel

class VMFactory (private val pref: UserPreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginVM::class.java)) {
            return LoginVM(pref) as T
        }

        if (modelClass.isAssignableFrom(LogoutVM::class.java)) {
            return LogoutVM(pref) as T
        }

        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(pref) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}