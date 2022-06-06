package com.belajar.android.storyapp1.Register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belajar.android.storyapp1.Preference.UserPreferences
import kotlinx.coroutines.launch

class RegisterViewModel(private val pref: UserPreferences) : ViewModel() {

    fun userRegister(name: String, email: String, password: String){
        viewModelScope.launch {
        }
    }
}