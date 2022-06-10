package com.belajar.android.storyapp1.Story


import androidx.lifecycle.*
import com.belajar.android.storyapp1.Model.UserModelData
import com.belajar.android.storyapp1.Preference.UserPreferences

class AllStoryVM(private val pref: UserPreferences): ViewModel() {
    fun getUser() : LiveData<UserModelData>{
        return pref.getUsers().asLiveData()
    }
}