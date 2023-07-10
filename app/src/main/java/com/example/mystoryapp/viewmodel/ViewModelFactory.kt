package com.example.mystoryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginPreference::class.java)) { return LoginPreference(pref) as T }
        throw IllegalArgumentException("Unknown ViewModel: " + modelClass.name)
    }
}