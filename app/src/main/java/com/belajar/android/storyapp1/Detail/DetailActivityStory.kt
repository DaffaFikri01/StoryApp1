package com.belajar.android.storyapp1.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.belajar.android.storyapp1.databinding.ActivityDetailStoryBinding

class DetailActivityStory : AppCompatActivity() {

    private lateinit var binding : ActivityDetailStoryBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}