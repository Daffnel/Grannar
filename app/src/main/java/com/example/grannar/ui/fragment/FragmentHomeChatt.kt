package com.example.grannar.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grannar.R

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
        val searchEditText: EditText = binding.etSearch


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val query = it.toString()
                    chatAdapter.filter(query)
                }
            }

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })


        fetchGroups()
    }


    //this just test for recylkerview

    private fun fetchGroups() {
        binding.progressBar.visibility = View.VISIBLE

        val testGroups = listOf(
            Group("1", "General Chat"),
            Group("2", "Friends Group"),
            Group("3", "Tech Enthusiasts"),
            Group("4", "Gaming Community"),
            Group("5", "Sports Fans"),
            Group("6", "Music Lovers"),
            Group("7", "Travel Enthusiasts"),
            Group("8", "Photography Club")
        )


        binding.rvChatGroups.layoutManager = LinearLayoutManager(requireContext())

        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressBar.visibility = View.GONE
            chatAdapter = GroupAdapter(testGroups) { group ->
                openGroupChat(group)
            }
            binding.rvChatGroups.adapter = chatAdapter
        }, 1500)
    }

    //this  fetch from firbase data you can take out aftr

  //  private fun fetchGroups() {
//        binding.progressBar.visibility = View.VISIBLE
//
//
//
//        firebaseManager.getAllGroups { groups ->
//
//            binding.progressBar.visibility = View.GONE
//            // If there are groups available
//            if (groups.isNotEmpty()) {
//                // Set up adapter to display groups in RecyclerView
//                chatAdapter = GroupAdapter(groups ) { group ->
//                    // Handle group item click (for example, open the group chat)
//                    openGroupChat(group)
//                }
//                binding.rvChatGroups.adapter = chatAdapter
//            } else {
//                // Handle empty groups, show a message or something
//                showNoGroupsMessage()
//            }
//        }
//
//
//    }
        // Handle opening the group chat
    private fun openGroupChat(group: Group) {
        val chatFragment = ChatFragment().apply {
            arguments = Bundle().apply {
                putString("GROUP_ID", group.groupId)
                putString("GROUP_NAME", group.groupName)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, chatFragment)
            .addToBackStack(null)
            .commit()
    }





    // Show a message when no groups are available
        private fun showNoGroupsMessage() {
            // This could show a TextView, Snackbar, or Toast
            Toast.makeText(requireContext(), "No groups available", Toast.LENGTH_SHORT).show()
        }







}


