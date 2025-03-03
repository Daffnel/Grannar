package com.example.grannar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grannar.data.firebase.FirebaseManager
import com.example.grannar.data.model.ChatMessage

class ChatViewModel : ViewModel() {

    private val firebaseManager = FirebaseManager()
    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> get() = _messages

    fun sendMessage(groupId: String, senderId: String, messageText: String) {
        if (messageText.isNotEmpty()) {
            firebaseManager.getCurrentUserName { senderName ->
                val message = ChatMessage(
                    senderId = senderId,
                    senderName = senderName,
                    message = messageText,
                    timestamp = System.currentTimeMillis()
                )

                firebaseManager.sendGroupMessage(groupId, message) { success, _ ->
                    if (success) {
                        listenForMessages(groupId)
                    }
                }
            }
        }
    }

    fun listenForMessages(groupId: String) {
        firebaseManager.getGroupMessages(groupId) { messages ->
            _messages.postValue(messages)
        }
    }
}
