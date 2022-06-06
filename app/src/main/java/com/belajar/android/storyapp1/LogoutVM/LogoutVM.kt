package com.belajar.android.storyapp1.LogoutVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belajar.android.storyapp1.Preference.UserPreferences
import kotlinx.coroutines.launch

class LogoutVM(private val pref: UserPreferences) : ViewModel() {
    fun userLogout(){
        viewModelScope.launch {
            pref.userLogout()
        }
    }
}