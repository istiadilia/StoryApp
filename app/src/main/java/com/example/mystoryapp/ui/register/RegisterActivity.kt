package com.example.mystoryapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        registerViewModel.message.observe(this) {
            checkRegisterUser(it, registerViewModel.isError)
        }

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnRegister.setOnClickListener {
            binding.apply {
                if (edRegisterEmail.isValidEmail && edRegisterPassword.isValidPass) {
                    val name = edRegisterName.text.toString()
                    val email = edRegisterEmail.text.toString()
                    val pass = edRegisterPassword.text.toString()

                    if (name.isEmpty()) {
                        edRegisterName.error = getString(R.string.input_name)
                        return@setOnClickListener
                    }

                    registerViewModel.getRegisterUser(name, email, pass)
                }
            }
        }
    }

    private fun checkRegisterUser(message: String, isError: Boolean) {
        if (!isError) {
            Toast.makeText(this, getString(R.string.user_create), Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            when (message) {
                "Bad Request" -> {
                    Toast.makeText(this, getString(R.string.email_taken), Toast.LENGTH_SHORT).show()
                    binding.edRegisterEmail.apply {
                        setText("")
                        requestFocus()
                    }
                }
                "timeout" -> {
                    Toast.makeText(this, getString(R.string.timeout), Toast.LENGTH_SHORT).show()
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}