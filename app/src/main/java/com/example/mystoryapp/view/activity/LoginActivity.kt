package com.example.mystoryapp.view.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.R
import com.example.mystoryapp.data.remote.response.LoginRequest
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.viewmodel.LoginViewModel
import com.example.mystoryapp.viewmodel.LoginPreference
import com.example.mystoryapp.viewmodel.UserPreference
import com.example.mystoryapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        playAnimation()

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.toRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (!isDataOk()) {
                Toast.makeText(this, getString(R.string.data_incorrect), Toast.LENGTH_SHORT).show()
            } else {
                val account = LoginRequest(email, password)
                loginViewModel.loginAccount(account)
            }
        }

        loginViewModel.message.observe(this) {
            when(it) {
                "Unauthorized" -> {
                    Toast.makeText(this, getString(R.string.unauthorized), Toast.LENGTH_SHORT).show()
                }
                "success" -> {
                    val pref = UserPreference.getInstance(dataStore)
                    val loginPreference = ViewModelProvider(this@LoginActivity, ViewModelFactory(pref))[LoginPreference::class.java]
                    loginViewModel.token.observe(this) { token ->
                        loginPreference.setToken(token)
                        loginPreference.setLoggedIn(true)
                        Toast.makeText(this, getString(R.string.success_login), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun isDataOk() = binding.edLoginEmail.isValidEmail && binding.edLoginPassword.isValidPass

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.storyAppLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}