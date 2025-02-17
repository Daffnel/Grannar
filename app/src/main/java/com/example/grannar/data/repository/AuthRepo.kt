package com.example.grannar.data.repository

import com.example.grannar.data.firebase.FirebaseManager

class AuthRepo(private val firebaseManager: FirebaseManager) {

    fun registerUser(email:String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseManager.registerUser(email, password, onResult)
    }

    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseManager.loginUser(email, password, onResult)
    }

}