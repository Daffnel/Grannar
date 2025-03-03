package com.example.grannar.data.model



data class ChatMessage(
    var senderId: String = "",
    var message: String = "",
    var timestamp: Long = 0L,
    var senderName: String = ""
) {
    constructor() : this("", "", 0L, "")
}