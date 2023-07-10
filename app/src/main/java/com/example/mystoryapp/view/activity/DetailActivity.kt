package com.example.mystoryapp.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mystoryapp.R
import com.example.mystoryapp.data.remote.response.ListStoryResponse
import com.example.mystoryapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_STORY = "extra_story"
    }
    private lateinit var binding: ActivityDetailBinding

    @SuppressLint("StringFormatInvalid")
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra("extra_story") as ListStoryResponse?

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_detail_story, story?.name)

        binding.apply {
            tvDetailName.text = getString(R.string.posted_by, story?.name)
            tvDetailDescription.text = story?.description.toString()
            Glide.with(this@DetailActivity)
                .load(story?.photoUrl)
                .into(ivDetailPhoto)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}