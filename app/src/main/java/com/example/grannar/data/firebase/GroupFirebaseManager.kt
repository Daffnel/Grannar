package com.example.grannar.data.firebase

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.grannar.data.Groups.CityGroups
import com.example.grannar.data.model.Group
import com.example.grannar.ui.activities.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GroupFirebaseManager {

    val db = Firebase.firestore
    val user = FirebaseAuth.getInstance().currentUser

    val userId = user?.uid          //Hämta användarens unika ID


    /**
     * Loggar ut användaren och återvänder till
     * första activityn samt tömmer backstacken
     */
    fun logout(context: Context) {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)   //Rensar backloggen så att det inte går att backa
    }


    /**
     * Hämtar alla grupper som matchar med användarens stad
     * Retunerar detta i en lista med Group objekt
     */
    suspend fun getGroupByCity(userCity: String): List<CityGroups> {

        return try {
            val query = db.collection("groups")
                .whereEqualTo("city", userCity)
                .get()
                .await()

            query.documents.mapNotNull { it.toObject(CityGroups::class.java) }

        } catch (e: Exception) {
            Log.e("!!!", "Kund inte hämnta några grupper i stad $userCity")
            return emptyList()
        }
    }


    /**
     *  Hämtar vilken stad Användren har registrerat
     *  rerunerar stad i en sträng
     */
    suspend fun getUserCity(): String? {

        if (userId != null) {
            try {
                val document = db.collection("users")
                    .document(userId)
                    .get()
                    .await()
                return document.getString("city")
            } catch (e: Exception) {
                return null
            }

        }
        return null
    }


    /**
     * Lägger till en ny grupp
     */
    fun addNewGroup(title: String, moreInfo: String, city: String) {
        val group: Group = Group(title, moreInfo, city)

        db.collection("groups")
            .add(group).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("!!!", "Ny grupp registrerad")
                } else {
                    Log.e("!!!", "Misslyckades med registrera ny grupp, task.exception")
                }

            }

    }
}