package com.example.mystoryapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.view.adapter.LoadingStateAdapter
import com.example.mystoryapp.view.adapter.StoryAdapter
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.viewmodel.*

@OptIn(ExperimentalPagingApi::class)
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels { ViewModelFactoryPaging() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.app_name)

        binding.rvStory.layoutManager = LinearLayoutManager(this@MainActivity)

        val pref = UserPreference.getInstance(dataStore)
        val loginPreference = ViewModelProvider(this@MainActivity, ViewModelFactory(pref))[LoginPreference::class.java]

        loginPreference.getToken().observe(this) {
            setStory(it)
        }

        binding.fab.setOnClickListener {
            addStory()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // to maps activity
            R.id.maps_menu-> startActivity(Intent(this@MainActivity, MapsActivity::class.java))
            // change language
            R.id.language_menu -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            // logout account
            R.id.logout_menu -> {
                val pref = UserPreference.getInstance(dataStore)
                val loginPreference = ViewModelProvider(this@MainActivity, ViewModelFactory(pref))[LoginPreference::class.java]
                loginPreference.setLoggedIn(false)
                loginPreference.setToken("")
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @ExperimentalPagingApi
    private fun setStory(token: String) {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.getAllStory(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun addStory() {
        startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
    }
}