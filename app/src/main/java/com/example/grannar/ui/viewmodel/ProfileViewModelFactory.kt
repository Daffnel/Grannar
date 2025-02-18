package com.example.grannar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grannar.data.firebase.FirebaseManager

class ProfileViewModelFactory(private val firebaseManager: FirebaseManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(firebaseManager) as T
        }
        throw IllegalArgumentException("Error getting ViewModel")
    }
}