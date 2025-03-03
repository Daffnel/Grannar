package com.example.grannar.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grannar.adapter.ChatAdapter
import com.example.grannar.adapter.GroupAdapter
import com.example.grannar.data.firebase.FirebaseManager
import com.example.grannar.data.model.ChatMessage
import com.example.grannar.databinding.FragmentChatBinding
import com.example.grannar.ui.viewmodel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var viewModel: ChatViewModel
    private lateinit var chatAdapter: ChatAdapter
    private var messagesList = mutableListOf<ChatMessage>()
    private var currentUserId = ""
    private var groupId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        arguments?.let {
            currentUserId = it.getString("currentUserId") ?: ""
            groupId = it.getString("GROUP_ID") ?: ""
        }

        if (groupId.isEmpty()) {
            Log.e("ChatFragment", "Group ID is empty")
            return
        }

        setupRecyclerView()
        setupObservers()
        viewModel.listenForMessages(groupId)

        binding.imageback.setOnClickListener {
            //back to fragmenthomechatt
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                viewModel.sendMessage(groupId, currentUserId, messageText)
                binding.messageInput.text.clear()
            }
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messagesList, currentUserId)
        binding.recyclerViewchatt.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
    }

    private fun setupObservers() {
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            messagesList.clear()
            messagesList.addAll(messages)
            chatAdapter.notifyDataSetChanged()
            binding.recyclerViewchatt.scrollToPosition(messagesList.size - 1)
        }
    }
}
