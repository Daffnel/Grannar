package com.example.grannar.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grannar.adapter.ChatAdapter
import com.example.grannar.data.firebase.FirebaseManager
import com.example.grannar.data.model.ChatMessage
import com.example.grannar.databinding.FragmentChatBinding
import java.util.UUID


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var chatId: String
    private lateinit var currentUserId: String
    private lateinit var receiverId: String
    private lateinit var groupName: String

    private val messagesList = mutableListOf<ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //reciv infor from ather fragment Bundle
        arguments?.let {
            currentUserId = it.getString("currentUserId") ?: ""
            receiverId = it.getString("receiverId") ?: ""
            groupName = it.getString("GROUP_NAME") ?: ""


        }

        // Set the group name in the UI
        binding.groupName.text = groupName

        // creat unic chatId
        chatId = generateUniqueChatId()

        firebaseManager = FirebaseManager()

        setupRecyclerView()
        listenForMessages()

        binding.sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messagesList, currentUserId)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
    }

    private fun sendMessage() {
        val messageText = binding.messageInput.text.toString().trim()
        if (messageText.isNotEmpty()) {
            val message = ChatMessage(
                senderId = currentUserId,
                receiverId = receiverId,
                message = messageText,
                timestamp = System.currentTimeMillis()
            )

            firebaseManager.sendMessage(chatId, message) { success, _ ->
                if (success) {
                    binding.messageInput.text.clear()
                }
            }
        }
    }

    private fun listenForMessages() {
        firebaseManager.getMessages(chatId) { messages ->
            messagesList.clear()
            messagesList.addAll(messages)
            chatAdapter.notifyDataSetChanged()
            binding.recyclerView.scrollToPosition(messagesList.size - 1)
        }
    }

    // generate unic ide
    private fun generateUniqueChatId(): String {
        return UUID.randomUUID().toString()
    }

}
