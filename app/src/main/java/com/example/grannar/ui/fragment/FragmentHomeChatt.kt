package com.example.grannar.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager

import com.example.grannar.adapter.GroupAdapter
import com.example.grannar.data.firebase.FirebaseManager
import com.example.grannar.data.model.Group
import com.example.grannar.databinding.FragmentHomeChattBinding


class FragmentHomeChatt : Fragment() {

    private lateinit var binding: FragmentHomeChattBinding
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var chatAdapter: GroupAdapter
    //private lateinit var allGroups: List<Group>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeChattBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseManager = FirebaseManager()


        chatAdapter = GroupAdapter(emptyList()) { group ->

        }

        binding.rvChatGroups.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }


        fetchGroups()



    }

    private fun fetchGroups() {
        binding.progressBar.visibility = View.VISIBLE
        firebaseManager.getAllGroups { groups ->

            binding.progressBar.visibility = View.GONE
            // If there are groups available
            if (groups.isNotEmpty()) {
                // Set up adapter to display groups in RecyclerView
                chatAdapter = GroupAdapter(groups) { group ->
                    // Handle group item click (for example, open the group chat)
                    openGroupChat(group)
                }
                binding.rvChatGroups.adapter = chatAdapter
            } else {
                // Handle empty groups, show a message or something
                showNoGroupsMessage()
            }
        }
    }
        // Handle opening the group chat
        private fun openGroupChat(group: Group) {
            val intent = Intent(requireContext(), ChatFragment::class.java).apply {
                putExtra("GROUP_ID", group.groupId)
                putExtra("GROUP_NAME", group.groupName)
            }
            startActivity(intent)
        }

        // Show a message when no groups are available
        private fun showNoGroupsMessage() {
            // This could show a TextView, Snackbar, or Toast
            Toast.makeText(requireContext(), "No groups available", Toast.LENGTH_SHORT).show()
        }




}


