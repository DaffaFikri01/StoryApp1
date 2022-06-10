package com.belajar.android.storyapp1.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.belajar.android.storyapp1.Model.Stories
import com.belajar.android.storyapp1.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide

class DetailActivityStory : AppCompatActivity() {

    private lateinit var binding : ActivityDetailStoryBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val story = intent.getParcelableExtra<Stories>(DETAIL)
        Glide.with(this)
            .load(story?.photoUrl)
            .into(binding.imgDetail)
        binding.userNameDetail.text = story?.name
        binding.userDescDetail.text = story?.description
    }

    companion object{
        const val DETAIL = "DETAIL"
    }
}