package com.example.grannar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grannar.data.repository.AuthRepo
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel(private val authRepo: AuthRepo) : ViewModel() {

    private val _authResult = MutableLiveData<Pair<Boolean, String?>>()
    val authResult: LiveData<Pair<Boolean, String?>> get() = _authResult

//    private val _userStatus = MutableLiveData<Boolean>()
//    val userStatus: LiveData<Boolean> get() = _userStatus
//
//    init {
//        checkUserStatus()
//    }

//    fun checkUserStatus(){
//        val user = FirebaseAuth.getInstance().currentUser
//        _userStatus.value = user != null
//    }

    fun registerUser(email: String, password: String){
        authRepo.registerUser(email, password) { success, message ->
            _authResult.value = Pair(success, message)
        }
    }

    fun loginUser(email: String, password: String){
        authRepo.loginUser(email, password) { success, message ->
            _authResult.value = Pair(success, message)
        }
    }

    fun logout(){
        authRepo.logout()
//        _userStatus.value = false
    }
}