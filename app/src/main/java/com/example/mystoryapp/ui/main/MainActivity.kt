package com.example.mystoryapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.AppPreferences
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.databinding.ItemListStoryBinding
import com.example.mystoryapp.api.response.ListStoryResponse
import com.example.mystoryapp.ui.story.AddStoryActivity
import com.example.mystoryapp.ui.story.DetailStoryActivity
import com.example.mystoryapp.ui.story.StoryAdapter
import com.example.mystoryapp.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var binding1: ItemListStoryBinding
    private val storyViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding1 = ItemListStoryBinding.inflate(layoutInflater)

        val appPreferences = AppPreferences(this)
        val token = appPreferences.authToken
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        storyViewModel.isLoading.observe(this) { showLoading(it) }
        storyViewModel.message.observe(this) { getStoryUser(storyViewModel.story) }

        if (token != null) { storyViewModel.getStories(token) }

        binding.fab.setOnClickListener { startActivity(Intent(this, AddStoryActivity::class.java)) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_menu -> {
                val appPreferences = AppPreferences(this)
                appPreferences.isLoggedIn = false
                appPreferences.authToken = null
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.languange_menu -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showNoData(isNoData: Boolean) {
        binding.noData.visibility = if (isNoData) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        val appPreferences = AppPreferences(this)
        val token = appPreferences.authToken

        if (token != null) {
            storyViewModel.getStories(token)
        }
    }

    private fun getStoryUser(story: List<ListStoryResponse>) {
        if (story.isEmpty()) {
            showNoData(true)
        } else {
            showNoData(false)
            val listUserAdapter = StoryAdapter(story)
            binding.rvStory.adapter = listUserAdapter

            listUserAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListStoryResponse) {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@MainActivity,
                        androidx.core.util.Pair.create(binding1.tvItemName, ViewCompat.getTransitionName(binding1.tvItemName)),
                        androidx.core.util.Pair.create(binding1.ivItemImage, ViewCompat.getTransitionName(binding1.ivItemImage))
                    )
                    val intent = Intent(this@MainActivity, DetailStoryActivity::class.java)
                    intent.putExtra(DetailStoryActivity.EXTRA_STORY, data)
                    startActivity(intent, options.toBundle())
                }
            })
        }
    }
}