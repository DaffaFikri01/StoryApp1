package com.belajar.android.storyapp1.Preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.belajar.android.storyapp1.Login.LoginResult
import com.belajar.android.storyapp1.Model.UserModelData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore : DataStore<Preferences>){

    fun getUsers() : Flow<UserModelData> {
        return dataStore.data.map { preferences ->
            UserModelData(
                preferences[TOKEN] ?: "",
                preferences[STATE] ?: false
            )
        }
    }

    suspend fun saveUser(user: UserModelData){
        dataStore.edit { preferences ->
            preferences[TOKEN] = user.token
            preferences[STATE] = user.isLogin
        }
    }

    suspend fun userLogout(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object{
        @Volatile

        private var INSTANCE: UserPreferences? = null

        private val TOKEN = stringPreferencesKey("token")
        private val STATE = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences{
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}