package com.example.grannar.data.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.grannar.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class FirebaseManager {
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val user = User(
                        id = userId,
                        email = email
                    )
                    saveNewUser(user) { success, message ->
                        onResult(success, message)
                    }
                } else{
                    onResult(false, task.exception?.message ?: "Failed to create user")
                }
            }
    }

    fun saveNewUser(user: User, onResult: (Boolean, String) -> Unit){
        //Get the users ID from Firebase Auth and return out of the function if no user is logged in
        val userId = auth.currentUser?.uid ?: return

        //Create new user document in Firestore
        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                onResult(true, "success")
            }
            .addOnFailureListener {
                onResult(false, "success")
            }
    }

    fun loginUser(email: String, password: String, onResult: (Boolean, String) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    onResult(true, "Login Successfully")
                } else{
                    onResult(false, "Login failed")
                }
            }
    }
    fun logout(){
        FirebaseAuth.getInstance().signOut()
    }
}

