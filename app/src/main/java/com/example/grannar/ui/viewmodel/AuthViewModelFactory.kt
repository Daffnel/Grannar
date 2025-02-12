package com.example.grannar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grannar.data.firebase.FirebaseManager
import com.example.grannar.data.repository.AuthRepo

class AuthViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(AuthRepo(FirebaseManager())) as T
        }
        throw IllegalArgumentException("Uknown ViewModel")
    }
}