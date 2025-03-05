package com.example.grannar.data.model

data class JoinRequest(
    val requestId: String = "",
    val userId: String = "",
    val groupId: String = "",
    val userName: String = "",
    val groupName: String = "",
    val status: String = "pending"
) {

    constructor() : this("", "", "", "", "","")
}