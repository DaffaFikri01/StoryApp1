package com.belajar.android.storyapp1.Story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.belajar.android.storyapp1.R
import com.belajar.android.storyapp1.databinding.ActivityAllUserStoryBinding

class AllUserStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAllUserStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllUserStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Story User"
    }
}