package com.example.grannar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grannar.data.repository.AuthRepo

class AuthViewModel(private val authRepo: AuthRepo) : ViewModel() {

    private val _authResult = MutableLiveData<Pair<Boolean, String?>>()
    val authResult: LiveData<Pair<Boolean, String?>> get() = _authResult

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
    }
}