package com.example.grannar.data.firebase

import android.util.Log
import com.example.grannar.data.model.ChatMessage
import com.example.grannar.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
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
        auth.signOut()
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
}

