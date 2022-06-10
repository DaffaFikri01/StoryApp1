package com.belajar.android.storyapp1.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.belajar.android.storyapp1.Detail.DetailActivityStory
import com.belajar.android.storyapp1.Model.Stories
import com.belajar.android.storyapp1.R
import com.belajar.android.storyapp1.databinding.ItemListStoryBinding
import com.bumptech.glide.Glide

class ListStoryAdapter(private val story : ArrayList<Stories>): RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemListStoryBinding) : RecyclerView.ViewHolder(binding.root) {
            val avatarImg: ImageView = itemView.findViewById(R.id.avatarImg)
            val tvUserName: TextView = itemView.findViewById(R.id.tv_userName)
            val tvUserDesc: TextView = itemView.findViewById(R.id.tv_userDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(story[position].photoUrl)
            .circleCrop()
            .into(holder.avatarImg)
        holder.tvUserName.text = story[position].name
        holder.tvUserDesc.text = story[position].description

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailActivityStory::class.java)
            intent.putExtra(DetailActivityStory.DETAIL, story[position])

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.avatarImg, "imgDetailTr"),
                    Pair(holder.tvUserName, "userNameTr"),
                    Pair(holder.tvUserDesc, "userDescTr")
                )
            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
    }


    override fun getItemCount(): Int = story.size
}