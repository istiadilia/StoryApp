package com.example.mystoryapp.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mystoryapp.R
import com.example.mystoryapp.viewmodel.UserPreference
import com.example.mystoryapp.viewmodel.LoginPreference
import com.example.mystoryapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        lifecycleScope.launch {
            delay(2500)
            withContext(Dispatchers.Main) {
                val pref = UserPreference.getInstance(dataStore)
                val loginPreference = ViewModelProvider(this@SplashScreenActivity, ViewModelFactory(pref)).get(LoginPreference::class.java)

                loginPreference.isLoggedIn().observe(this@SplashScreenActivity) { isLoggedIn ->
                    val intent = if (isLoggedIn) {
                        Intent(this@SplashScreenActivity, MainActivity::class.java)
                    } else {
                        Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
