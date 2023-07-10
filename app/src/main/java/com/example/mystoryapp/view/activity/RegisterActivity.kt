package com.example.mystoryapp.view.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.R
import com.example.mystoryapp.data.remote.response.RegisterRequest
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        playAnimation()

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.toLogin.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (!isDataOk()) {
                Toast.makeText(this, getString(R.string.data_incorrect), Toast.LENGTH_SHORT).show()
            } else {
                val account = RegisterRequest(name, email, password)
                registerViewModel.registerAccount(account)
            }

        }

        registerViewModel.message.observe(this) {
            when(it) {
                "User created" -> {
                    Toast.makeText(this, getString(R.string.user_create), Toast.LENGTH_SHORT).show()
                    finish()
                }
                "Bad Request" -> {
                    Toast.makeText(this, getString(R.string.email_taken), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isDataOk() = binding.edRegisterName.isValidName && binding.edRegisterEmail.isValidEmail && binding.edRegisterPassword.isValidPass

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