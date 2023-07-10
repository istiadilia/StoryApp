package com.example.mystoryapp.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.databinding.ItemListStoryBinding
import com.example.mystoryapp.data.remote.response.ListStoryResponse
import com.example.mystoryapp.view.activity.DetailActivity

class StoryAdapter : PagingDataAdapter<ListStoryResponse, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryResponse>() {
            override fun areItemsTheSame(oldItem: ListStoryResponse, newItem: ListStoryResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryResponse, newItem: ListStoryResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListStoryBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    inner class ListViewHolder(private val binding: ItemListStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryResponse) {
            binding.apply {
                tvItemName.text = item.name
                Glide.with(ivItemImage.context)
                    .load(item.photoUrl)
                    .into(ivItemImage)
                root.setOnClickListener {
                    val intent = Intent(binding.root.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_STORY, item)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

}