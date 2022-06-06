package com.belajar.android.storyapp1.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.belajar.android.storyapp1.Model.UserModelData
import com.belajar.android.storyapp1.Preference.UserPreferences
import kotlinx.coroutines.launch

class LoginVM(private val pref: UserPreferences) : ViewModel() {
    fun loginUsers() : LiveData<UserModelData> {
        return pref.getUsers().asLiveData()
    }

    fun sessionSave(user: UserModelData){
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}