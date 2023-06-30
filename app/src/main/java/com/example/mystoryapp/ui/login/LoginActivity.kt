package com.example.mystoryapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.mystoryapp.AppPreferences
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.ui.register.RegisterActivity
import com.example.mystoryapp.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appPreferences = AppPreferences(this)

        loginViewModel.message.observe(this) {
            val user = loginViewModel.userLogin.value
            checkLoginUser(it, loginViewModel.isError, user?.loginResult?.token, appPreferences)
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        if (appPreferences.isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.apply {
            btnLogin.setOnClickListener {
                if (edLoginEmail.isValidEmail && edLoginPassword.isValidPass) {
                    val email = edLoginEmail.text.toString()
                    val pass = edLoginPassword.text.toString()
                    loginViewModel.getLoginUser(email, pass)
                }
            }

            signIn.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun checkLoginUser(message: String, isError: Boolean, token: String?, preference: AppPreferences) {
        if (!isError) {
            Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
            ).show()
            preference.isLoggedIn = true
            if (token != null) preference.authToken = token
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            when (message) {
                "Unauthorized" -> {
                    Toast.makeText(this, getString(R.string.unauthorized), Toast.LENGTH_SHORT)
                        .show()
                    binding.edLoginEmail.apply {
                        setText("")
                        requestFocus()
                    }
                    binding.edLoginPassword.setText("")
                }
                "timeout" -> {
                    Toast.makeText(this, getString(R.string.timeout), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    Toast.makeText(
                        this,
                        "${getString(R.string.error_message)} $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
