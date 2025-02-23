package com.example.grannar.data.firebase

import android.util.Log

import com.example.grannar.data.model.ChatMessage
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.grannar.data.model.Group
import com.example.grannar.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class FirebaseManager {
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    //Function to register the user with firebase authentication also calls the saveNewUser if successful
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

    //Function to save the user to firestore database with userId and Email
    fun saveNewUser(user: User, onResult: (Boolean, String) -> Unit){
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

    //Function to login with firebase authentication
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
        auth.signOut()
    }

    //Function to update the other fields of the user thats logged in thru the profile
    fun updateUserProfile(name: String, age: Int, city: String, bio: String, interests: List<String>, callback: (Boolean, String?) -> Unit){
        val userId = auth.currentUser?.uid ?: return

        val userUpdate = mapOf(
            "name" to name,
            "age" to age,
            "city" to city,
            "bio" to bio,
            "interests" to interests
        )

        db.collection("users").document(userId)
            .update(userUpdate)
            .addOnSuccessListener {
                callback(true, "Profile updated!")
            }
            .addOnFailureListener { e ->
                callback(false, e.localizedMessage)
            }
    }

    //Function to get the currently logged in user to update the information fields in the profile
    fun getUserProfile(callback: (User?) -> Unit){
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                callback(user)
            }
            .addOnFailureListener {
                callback(null)
            }
    }


    //fun sendmessage

    fun sendMessage(chatId: String, message: ChatMessage, onResult: (Boolean, String) -> Unit) {
        val db = Firebase.firestore

        //  Firestore
        db.collection("chats").document(chatId).collection("messages")
            .add(message)
            .addOnSuccessListener {
                onResult(true, "Message sent successfully")
            }
            .addOnFailureListener { exception ->
                onResult(false, "Failed to send message: ${exception.message}")
            }
    }
//fun recivemessge

    fun getMessages(chatId: String, onResult: (List<ChatMessage>) -> Unit) {
        val db = Firebase.firestore
        db.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e("Chat", "Failed to load messages: ${error.message}")
                    return@addSnapshotListener
                }
                val messages = snapshots?.documents?.map { it.toObject(ChatMessage::class.java)!! } ?: emptyList()
                onResult(messages)
            }
    }

    // fun creatgruochat
    fun createGroupChat(groupName: String, members: List<String>, creatorId: String, onResult: (Boolean, String) -> Unit) {
        val db = Firebase.firestore
        val groupId = db.collection("groupChats").document().id

        val groupData = mapOf(
            "groupId" to groupId,
            "groupName" to groupName,
            "members" to members,
            "creatorId" to creatorId  //  administrator id
        )

        db.collection("groupChats").document(groupId)
            .set(groupData)
            .addOnSuccessListener {
                onResult(true, "Group created successfully")
            }
            .addOnFailureListener { exception ->
                onResult(false, "Failed to create group: ${exception.message}")
            }
    }


    //sendgrupmessage

    fun sendGroupMessage(groupId: String, message: ChatMessage, onResult: (Boolean, String) -> Unit) {
        val db = Firebase.firestore

        db.collection("groupChats").document(groupId).collection("messages")
            .add(message)
            .addOnSuccessListener {
                onResult(true, "Message sent successfully")
            }
            .addOnFailureListener { exception ->
                onResult(false, "Failed to send message: ${exception.message}")
            }
    }

    // getmessage on grupp
    fun getGroupMessages(groupId: String, onResult: (List<ChatMessage>) -> Unit) {
        val db = Firebase.firestore

        db.collection("groupChats").document(groupId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e("GroupChat", "Failed to load messages: ${error.message}")
                    return@addSnapshotListener
                }

                val messages = snapshots?.documents?.map { it.toObject(ChatMessage::class.java)!! } ?: emptyList()
                onResult(messages)
            }
    }

    //removegrupp

    fun removeMemberFromGroup(groupId: String, userId: String, currentUserId: String, onResult: (Boolean, String) -> Unit) {
        val db = Firebase.firestore

        // Fetch the group data to check if the current user is the admin
        db.collection("groupChats").document(groupId).get()
            .addOnSuccessListener { document ->
                val creatorId = document.getString("creatorId") // creatorId (admin)
                if (creatorId == currentUserId) {
                    //  admin, proceed to remove the member
                    db.collection("groupChats").document(groupId)
                        .update("members", FieldValue.arrayRemove(userId))
                        .addOnSuccessListener {
                            onResult(true, "Member removed successfully")
                        }
                        .addOnFailureListener { exception ->
                            onResult(false, "Failed to remove member: ${exception.message}")
                        }
                } else {
                    onResult(false, "Only the group creator can remove members")
                }
            }
            .addOnFailureListener { exception ->
                onResult(false, "Failed to fetch group data: ${exception.message}")
            }
    }
    // only admin can change name grupp

    fun changeGroupName(groupId: String, newGroupName: String, currentUserId: String, onResult: (Boolean, String) -> Unit) {
        val db = Firebase.firestore

        db.collection("groupChats").document(groupId).get()
            .addOnSuccessListener { document ->
                val creatorId = document.getString("creatorId") // creatorId (admin)
                if (creatorId == currentUserId) {
                    //  admin, proceed to change group name
                    db.collection("groupChats").document(groupId)
                        .update("groupName", newGroupName)
                        .addOnSuccessListener {
                            onResult(true, "Group name changed successfully")
                        }
                        .addOnFailureListener { exception ->
                            onResult(false, "Failed to change group name: ${exception.message}")
                        }
                } else {
                    onResult(false, "Only the group creator can change the group name")
                }
            }
            .addOnFailureListener { exception ->
                onResult(false, "Failed to fetch group data: ${exception.message}")
            }
    }

    // إadd member to grupp
    fun addMemberToGroup(groupId: String, userId: String, currentUserId: String, onResult: (Boolean, String) -> Unit) {
        val db = Firebase.firestore

        db.collection("groupChats").document(groupId).get()
            .addOnSuccessListener { document ->
                val creatorId = document.getString("creatorId") // creatorId (admin)
                if (creatorId == currentUserId) {
                    // admin, proceed to add the member
                    db.collection("groupChats").document(groupId)
                        .update("members", FieldValue.arrayUnion(userId))
                        .addOnSuccessListener {
                            onResult(true, "Member added successfully")
                        }
                        .addOnFailureListener { exception ->
                            onResult(false, "Failed to add member: ${exception.message}")
                        }
                } else {
                    onResult(false, "Only the group creator can add members")
                }
            }
            .addOnFailureListener { exception ->
                onResult(false, "Failed to fetch group data: ${exception.message}")
            }
    }

    // Manage group rules (admin only)
    fun setGroupRules(groupId: String, rules: String, currentUserId: String, onResult: (Boolean, String) -> Unit) {
        val db = Firebase.firestore

        db.collection("groupChats").document(groupId).get()
            .addOnSuccessListener { document ->
                val creatorId = document.getString("creatorId") // creatorId (admin)
                if (creatorId == currentUserId) {
                    // admin, proceed to set group rules
                    db.collection("groupChats").document(groupId)
                        .update("rules", rules)
                        .addOnSuccessListener {
                            onResult(true, "Group rules set successfully")
                        }
                        .addOnFailureListener { exception ->
                            onResult(false, "Failed to set group rules: ${exception.message}")
                        }
                } else {
                    onResult(false, "Only the group creator can set group rules")
                }
            }
            .addOnFailureListener { exception ->
                onResult(false, "Failed to fetch group data: ${exception.message}")
            }
    }
    //getall gruppchatt
    fun getAllGroups(onResult: (List<Group>) -> Unit) {
        db.collection("groupChats")
            .get()
            .addOnSuccessListener { documents ->
                val groups = documents.mapNotNull { it.toObject(Group::class.java) }
                onResult(groups)
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseManager", "Failed to fetch groups: ${exception.message}")
                onResult(emptyList()) // Return empty list if there's an error
            }
    }




}

