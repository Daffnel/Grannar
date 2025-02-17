package com.example.grannar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grannar.data.firebase.FirebaseManager
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel(private val firebaseManager: FirebaseManager) : ViewModel() {

    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> get() = _updateResult

    fun updateProfile(name: String, age: Int, city: String, bio: String, interests: List<String>){
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        firebaseManager.updateUserProfile(userId, name, age, city, bio, interests) { success ->
            _updateResult.value = success
        }
    }
}