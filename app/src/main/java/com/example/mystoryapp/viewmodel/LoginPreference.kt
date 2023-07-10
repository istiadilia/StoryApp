package com.example.mystoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginPreference(private val pref: UserPreference) : ViewModel() {
    fun isLoggedIn(): LiveData<Boolean> = pref.isLoggedIn().asLiveData()

    fun setLoggedIn(isLoggedIn: Boolean) {
        viewModelScope.launch {
            pref.setLoggedIn(isLoggedIn)
        }
    }

    fun getToken(): LiveData<String> = pref.getToken().asLiveData()

    fun setToken(token: String) {
        viewModelScope.launch {
            pref.setToken(token)
        }
    }
}