package com.example.grannar.data.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.grannar.data.Groups.CityGroups
import com.example.grannar.data.model.Group
import com.example.grannar.ui.activities.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class CityGroupRepository {

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

    suspend fun getUserID(): String{
        if(userId != null){
            return userId
        }else {
            return "Missing ID"
        }
    }

    /**
     * Hämtar alla grupper som matchar med användarens stad
     * Retunerar detta i en lista med Group objekt
     */

    fun getGroupByCity(userCity: String, callback: (List<CityGroups>) -> Unit){
        db.collection("groups")
            .whereEqualTo("city",userCity)
            .get()
            .addOnSuccessListener { query ->
                val groups = query.documents.mapNotNull { it.toObject(CityGroups::class.java) }
                callback(groups)
            }
            .addOnFailureListener { e ->
                Log.e("!!!","Could not get groups for ${userCity}",e)
            }
    }



    /**
     *  Hämtar vilken stad Användaren har registrerat
     *  returnerar stad i en sträng
     */
    fun getUserCity(callback: (String?)-> Unit){
        if(userId != null){
            db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    callback(document.getString("city"))
                }
                .addOnFailureListener {
                    callback(null)
                }
        }else {
            callback(null)
        }
    }


    /**
     * Lägger till en ny grupp
     */
    fun addNewGroup(title: String, moreInfo: String, city: String) {
        val group: Group = Group(title, moreInfo, city)

        db.collection("groups")
            .add(group).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("!!!", "New group registered")
                } else {
                    Log.e("!!!", "Failed to register new group, task.exception")
                }

            }

    }

    fun joinGrupp(groupId: String, userId: String){




        db.collection("groups").document(groupId)
            .update("members", FieldValue.arrayUnion(userId))
            .addOnSuccessListener {
                Log.d("!!!","New member added to the group")
            }
            .addOnFailureListener { e ->
                Log.e("!!!","Failed to add member to the group",e)
            }
    }
}