package com.belajar.android.storyapp1.Story

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.belajar.android.storyapp1.MainActivity
import com.belajar.android.storyapp1.LogoutVM.LogoutVM
import com.belajar.android.storyapp1.Preference.UserPreferences
import com.belajar.android.storyapp1.R
import com.belajar.android.storyapp1.VMFactory
import com.belajar.android.storyapp1.databinding.ActivityAllUserStoryBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class AllUserStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAllUserStoryBinding
    private lateinit var logoutVM: LogoutVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllUserStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Story User"

        setupRegisVM()

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRegisVM() {
        logoutVM = ViewModelProvider(this,
            VMFactory(UserPreferences.getInstance(dataStore)))[LogoutVM::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.logout -> {
                logoutVM.userLogout()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                true
            }

            R.id.settingLanguage -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> true
        }
    }

}