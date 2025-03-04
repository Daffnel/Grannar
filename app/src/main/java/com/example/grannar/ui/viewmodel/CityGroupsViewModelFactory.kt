package com.example.grannar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grannar.data.firebase.FirebaseManager

import com.example.grannar.data.repository.CityGroupsViewModel

class CityGroupsViewModelFactory(private val repository: FirebaseManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityGroupsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityGroupsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
