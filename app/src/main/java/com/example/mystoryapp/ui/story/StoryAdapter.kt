package com.example.mystoryapp.ui.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.databinding.ItemListStoryBinding
import com.example.mystoryapp.api.response.ListStoryResponse

class StoryAdapter(private val listStory: List<ListStoryResponse>) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryResponse)
    }

    class ListViewHolder(var binding: ItemListStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, photoUrl) = listStory[position]
        holder.binding.apply {
            tvItemName.text = name
            Glide.with(holder.itemView.context)
                .load(photoUrl)
                .into(ivItemImage)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(listStory[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int = listStory.size
}