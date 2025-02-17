package com.example.grannar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class UserStatusViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userStatus = MutableLiveData<Boolean>()
    val userStatus: LiveData<Boolean> get() = _userStatus

    init {
        _userStatus.value = auth.currentUser != null

        auth.addAuthStateListener { firebaseAuth ->
            _userStatus.value = firebaseAuth.currentUser != null
        }
    }

    fun logout(){
        auth.signOut()
    }
}