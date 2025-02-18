package com.example.grannar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grannar.data.firebase.FirebaseManager
import com.example.grannar.data.model.User
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel(private val firebaseManager: FirebaseManager) : ViewModel() {

    private val _updateStatus = MutableLiveData<Pair<Boolean, String?>>()
    val updateStatus: LiveData<Pair<Boolean, String?>> get() = _updateStatus

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> get() = _userProfile

    fun updateUserProfile(name: String, age: Int, city: String, bio: String, interest: List<String>){
        firebaseManager.updateUserProfile(name, age, city, bio, interest){ success, message ->
            _updateStatus.value = Pair(success, message)
        }
    }
    fun getUserProfile(){
        firebaseManager.getUserProfile { user ->
            _userProfile.value = user
        }
    }
}