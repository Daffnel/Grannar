package com.example.grannar.data.firebase

import android.annotation.SuppressLint
import android.util.Log

import com.example.grannar.data.model.ChatMessage
import com.example.grannar.data.Groups.CityGroups
import com.example.grannar.data.model.Group
import com.example.grannar.data.model.JoinRequest
import com.example.grannar.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FirebaseManager {
    private val dbb = Firebase.firestore
    private val db = FirebaseFirestore.getInstance()
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
                } else {
                    onResult(false, task.exception?.message ?: "Failed to create user")
                }
            }
    }

    //Function to save the user to firestore database with userId and Email
    fun saveNewUser(user: User, onResult: (Boolean, String) -> Unit) {
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
    fun loginUser(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Login successful")
                } else {
                    onResult(false, "Login failed")
                }
            }
    }


    fun logout() {
        auth.signOut()
    }

    //Function to update the other fields of the user that's logged in through the profile
    fun updateUserProfile(
        name: String,
        age: Int,
        city: String,
        bio: String,
        interests: List<String>,
        callback: (Boolean, String?) -> Unit
    ) {
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
    fun getUserProfile(callback: (User?) -> Unit) {
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

    fun sendGroupMessage(groupId: String, message: ChatMessage, callback: (Boolean, String?) -> Unit) {
        val ref = db.collection("groups").document(groupId).collection("messages").document()
        ref.set(message)
            .addOnSuccessListener { callback(true, null) }
            .addOnFailureListener { callback(false, it.message) }
    }

    fun getGroupMessages(groupId: String, callback: (List<ChatMessage>) -> Unit) {
        db.collection("groups").document(groupId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    callback(emptyList())
                    return@addSnapshotListener
                }

                val messages = snapshot.toObjects(ChatMessage::class.java)
                callback(messages)
            }
    }

    fun getCurrentUserName(callback: (String) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val name = document.getString("name") ?: "Unknown"
                callback(name)
            }
            .addOnFailureListener {
                callback("Unknown")
            }
    }
    //getall gruppchatt
//    fun getAllGroups(onResult: (List<Group>) -> Unit) {
//        db.collection("groupChats")
//            .get()
//            .addOnSuccessListener { documents ->
//                val groups = documents.mapNotNull { it.toObject(Group::class.java) }
//                onResult(groups)
//            }
//            .addOnFailureListener { exception ->
//                Log.e("FirebaseManager", "Failed to fetch groups: ${exception.message}")
//                onResult(emptyList()) // Return empty list if there's an error
//            }
//    }

    //get all groups
    fun getAllCityGroups(callback: (List<CityGroups>) -> Unit) {
        db.collection("groups")
            .get()
            .addOnSuccessListener { query ->
                val groups = query.documents.mapNotNull { document ->
                    CityGroups(
                        id = document.id,
                        title = document.getString("title") ?: "",
                        moreInfo = document.getString("moreInfo") ?: "",
                        city = document.getString("city") ?: "",
                        adminId = document.getString("adminId") ?: "",
                        members = document.get("members") as? List<String> ?: emptyList()
                    )
                }
                callback(groups)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseManager", "Failed to fetch city groups", e)
                callback(emptyList())
            }
    }

    /**
     * Get all groups that matches users profile City.
     *  Returns a List
     */
    fun getGroupsByUsersCity(callback: (List<CityGroups>) -> Unit) {

        var userCity = ""
        val userId: String? = auth.uid

        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    userCity = document.getString("city").toString()
                    db.collection("groups")
                        .whereEqualTo("city", userCity)
                        .get()
                        .addOnSuccessListener { query ->
                            val groups =
                                query.documents.mapNotNull { it.toObject(CityGroups::class.java) }
                            Log.d("!!!", "Found ${groups.size} groups in $userCity")
                            callback(groups)
                        }
                        .addOnFailureListener { e ->
                            Log.e("!!!", "Failed to fetch groups for: $userCity", e)
                            callback(emptyList())
                        }
                }
                .addOnFailureListener {
                    Log.e("!!!", "Failed to fetch user's city", it)
                    callback(emptyList())
                }

        }

    }

    /**
     * Get users city
     */

    fun getUserCity(callback: (String) -> Unit) {

        val userId: String? = auth.uid

        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val city = document.getString("city")
                        if (city != null) {
                            callback(city)
                        }
                    } else {
                        callback("") // Användaren finns inte
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Failed to fetch city", e)
                    callback("")
                }
        }


    }

    /**
     * Adds a new group
     */
    fun addNewGroup(title: String, moreInfo: String, city: String) {
        val userId = auth.currentUser?.uid ?: return

        val newGroup = db.collection("groups").document()
        val group = CityGroups(
            id = newGroup.id,
            title = title,
            moreInfo = moreInfo,
            city = city,
            adminId = userId,
            members = listOf(userId)
        )

        newGroup.set(group)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("!!!", "New group registered")
                } else {
                    Log.e("!!!", "Failed to register new group", task.exception)
                }
            }
    }


    // Deletes a group if the current user is the admin
    fun deleteGroupIfAdmin(groupId: String, callback: (Boolean, String?) -> Unit) {
        // Validate groupId
        if (groupId.isNullOrEmpty()) {
            Log.e("FirebaseManager", "Group ID is null or empty")
            callback(false, "Invalid group ID")
            return
        }

        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.e("FirebaseManager", "User is not logged in")
            callback(false, "User is not logged in")
            return
        }

        val groupRef = db.collection("groups").document(groupId)

        // Fetch the group document
        groupRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val adminId = document.getString("adminId") ?: ""
                if (adminId == userId) {
                    // Current user is the admin, delete the group
                    groupRef.delete()
                        .addOnSuccessListener {
                            Log.d("FirebaseManager", "Group deleted successfully")
                            callback(true, "Group deleted successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirebaseManager", "Failed to delete group", e)
                            callback(false, "Failed to delete group: ${e.message}")
                        }
                } else {
                    Log.e("FirebaseManager", "User is not the admin of the group")
                    callback(false, "You are not the admin of this group")
                }
            } else {
                Log.e("FirebaseManager", "Group document does not exist")
                callback(false, "Group does not exist")
            }
        }.addOnFailureListener { e ->
            Log.e("FirebaseManager", "Failed to fetch group document", e)
            callback(false, "Failed to fetch group details: ${e.message}")
        }
    }
    //Function to join a group by adding current userId to members field in groups
    fun joinGroup(groupId: String, userId: String, callback: (Boolean) -> Unit){
        val groupRef = db.collection("groups").document(groupId)

        groupRef.get()
            .addOnSuccessListener { document ->
                val members = document.get("members") as? List<String> ?: listOf()

                //Check so the user is not already a member
                if (members.contains(userId)){
                    Log.e("!!!", "User already member")
                    callback(false)
                } else {
                    groupRef.update("members", FieldValue.arrayUnion(userId))
                        .addOnSuccessListener {
                            Log.d("!!!", "User has joined the group")
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            Log.e("!!!", "Failed to join group", e)
                            callback(false)
                        }
                }
            }
    }


    //Get all the groups where the user is a member
    fun getGroupsWhenMember(callback: (List<CityGroups>) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null){
            callback(emptyList())
            return
        }

        db.collection("groups")
            .whereArrayContains("members", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val groups = querySnapshot.documents.mapNotNull { it.toObject(CityGroups::class.java) }
                callback(groups)
            }
            .addOnFailureListener { e ->
                Log.e("!!!", "Failed to get groups", e)
                callback(emptyList())
            }
    }
    //check if you are members
    fun isUserMemberOfGroup(groupId: String, userId: String, callback: (Boolean) -> Unit) {
        db.collection("groups").document(groupId)
            .get()
            .addOnSuccessListener { document ->
                val members = document.get("members") as? List<String> ?: emptyList()
                callback(members.contains(userId))
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseManager", "Failed to check group membership", e)
                callback(false)
            }
    }
    //get name user
    fun getUserName(userId: String, callback: (String) -> Unit) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val name = document.getString("name") ?: "Unknown User"
                callback(name)
            }
            .addOnFailureListener {
                callback("Unknown User")
            }
    }
    //send enjoy members
    fun sendJoinRequest(groupId: String, userId: String, userName: String, groupName: String, callback: (Boolean) -> Unit) {
        val joinRequest = hashMapOf(
            "userId" to userId,
            "groupId" to groupId,
            "userName" to userName,
            "groupName" to groupName,
            "status" to "pending"
        )

        db.collection("join_requests")
            .add(joinRequest)
            .addOnSuccessListener { documentReference ->
                val requestId = documentReference.id
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    //get requst enjo
    fun getJoinRequestsForAdmin(callback: (List<JoinRequest>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        db.collection("groups")
            .whereEqualTo("adminId", userId)
            .get()
            .addOnSuccessListener { groupsSnapshot ->
                val groupIds = groupsSnapshot.documents.map { it.id }

                if (groupIds.isNotEmpty()) {
                    db.collection("join_requests")
                        .whereIn("groupId", groupIds)
                        .whereEqualTo("status", "pending")
                        .get()
                        .addOnSuccessListener { requestsSnapshot ->
                            val requests = requestsSnapshot.documents.map { document ->
                                JoinRequest(
                                    requestId = document.id,
                                    userId = document.getString("userId") ?: "",
                                    groupId = document.getString("groupId") ?: "",
                                    userName = document.getString("userName") ?: "",
                                    groupName = document.getString("groupName") ?: "",
                                    status = document.getString("status") ?: "pending"
                                )
                            }
                            callback(requests)
                        }
                        .addOnFailureListener {
                            callback(emptyList())
                        }
                } else {
                    callback(emptyList())
                }
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    // accepet add usser requsr in groupp

    fun approveJoinRequest(groupId: String, userId: String, callback: (Boolean) -> Unit) {
        val groupRef = db.collection("groups").document(groupId)
        groupRef.update("members", FieldValue.arrayUnion(userId))
            .addOnSuccessListener {

                db.collection("join_requests")
                    .whereEqualTo("groupId", groupId)
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            document.reference.update("status", "approved")
                        }
                        callback(true)
                    }
                    .addOnFailureListener {
                        callback(false)
                    }
            }
            .addOnFailureListener {
                callback(false)
            }
    }


    //recject add user to grupp

    fun rejectJoinRequest(requestId: String, callback: (Boolean) -> Unit) {
        if (requestId.isNotEmpty()) {
            Log.d("FirebaseManager", "Rejecting request with ID: $requestId")
            db.collection("join_requests")
                .document(requestId)
                .update("status", "rejected")
                .addOnSuccessListener {
                    Log.d("FirebaseManager", "Request rejected successfully")
                    callback(true)
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseManager", "Failed to reject request", e)
                    callback(false)
                }
        } else {
            Log.e("FirebaseManager", "Invalid request ID: $requestId")
            callback(false)
        }
    }

    //Function to leave a group and remove the user from the members list on Firebase
    fun leaveGroup(groupId: String, callback: (Boolean) -> Unit) {
        // Validate groupId
        if (groupId.isNullOrEmpty()) {
            Log.e("FirebaseManager", "Group ID is null or empty")
            callback(false)
            return
        }

        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.e("FirebaseManager", "User is not logged in")
            callback(false)
            return
        }

        val groupRef = db.collection("groups").document(groupId)

        groupRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val members = document.get("members") as? List<String> ?: listOf()
                if (members.contains(userId)) {
                    groupRef.update("members", FieldValue.arrayRemove(userId))
                        .addOnSuccessListener {
                            Log.d("FirebaseManager", "User left the group")
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirebaseManager", "Failed to leave group", e)
                            callback(false)
                        }
                } else {
                    Log.e("FirebaseManager", "User is not a member of the group")
                    callback(false)
                }
            } else {
                Log.e("FirebaseManager", "Group document does not exist")
                callback(false)
            }
        }.addOnFailureListener { e ->
            Log.e("FirebaseManager", "Failed to fetch group document", e)
            callback(false)
        }
    }
}

