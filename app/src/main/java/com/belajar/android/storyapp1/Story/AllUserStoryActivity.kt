package com.belajar.android.storyapp1.Story

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.belajar.android.storyapp1.Adapter.ListStoryAdapter
import com.belajar.android.storyapp1.Api.ApiConfig
import com.belajar.android.storyapp1.MainActivity
import com.belajar.android.storyapp1.LogoutVM.LogoutVM
import com.belajar.android.storyapp1.Model.Stories
import com.belajar.android.storyapp1.Preference.UserPreferences
import com.belajar.android.storyapp1.R
import com.belajar.android.storyapp1.VMFactory
import com.belajar.android.storyapp1.databinding.ActivityAllUserStoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class AllUserStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAllUserStoryBinding
    private lateinit var logoutVM: LogoutVM
    private lateinit var allStoryVM: AllStoryVM
    private lateinit var adapter: ListStoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllUserStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.storyUser)

        setupLogoutVM()
        setupAllStoryVM()
        setupGetStory()

        binding.apply {
            rvAllStory.layoutManager = LinearLayoutManager(this@AllUserStoryActivity)
            rvAllStory.setHasFixedSize(true)
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
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

    private fun setupAllStoryVM(){
        allStoryVM = ViewModelProvider(this,
            VMFactory(UserPreferences.getInstance(dataStore))) [AllStoryVM::class.java]
    }

    private fun setupLogoutVM() {
        logoutVM = ViewModelProvider(this,
            VMFactory(UserPreferences.getInstance(dataStore))) [LogoutVM::class.java]
    }

    private fun setupGetStory() {
        showLoading(true)

        allStoryVM.getUser().observe(this ) { stories ->
            if(stories != null) {
                ApiConfig.getApiService().getAllStory("Bearer " + stories.token)
                    .enqueue(object: Callback<GetAllStoryResponse> {
                    override fun onResponse(
                        call: Call<GetAllStoryResponse>,
                        response: Response<GetAllStoryResponse>
                    ) {
                        showLoading(false)
                        if(response.isSuccessful) {
                            getStories(response.body()?.listStory!!)
                            Toast.makeText(
                                this@AllUserStoryActivity,
                                resources.getString(R.string.loadSuccessful),
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                        showLoading(false)
                        Toast.makeText(
                            this@AllUserStoryActivity,
                            resources.getString(R.string.loadFail),
                            Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }

    private fun getStories(list: List<ListStoryItem>) {
        val listStories = ArrayList<Stories>()
        for(item in list) {
            val story = Stories(
                item.photoUrl,
                item.name,
                item.description
            )
            listStories.add(story)
        }
        adapter = ListStoryAdapter(listStories)
        binding.rvAllStory.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}