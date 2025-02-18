package com.example.grannar.data.model

data class ChatMessage(
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timestamp: Long
)
