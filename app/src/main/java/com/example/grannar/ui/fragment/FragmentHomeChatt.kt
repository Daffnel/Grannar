package com.example.grannar.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grannar.R

import com.example.grannar.adapter.GroupAdapter
import com.example.grannar.data.Groups.CityGroups
import com.example.grannar.data.firebase.FirebaseManager
import com.example.grannar.data.model.Group
import com.example.grannar.databinding.FragmentHomeChattBinding
import com.example.grannar.ui.viewmodel.GroupViewModel
import com.google.firebase.auth.FirebaseAuth


class FragmentHomeChatt : Fragment() {

    private lateinit var binding: FragmentHomeChattBinding
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var chatAdapter: GroupAdapter
    private lateinit var groupViewModel: GroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeChattBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        groupViewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
        firebaseManager = FirebaseManager()


        chatAdapter = GroupAdapter(emptyList()) { group ->
            openGroupChat(group)
        }

        groupViewModel.groupList.observe(viewLifecycleOwner) { groups ->
            if (groups.isNotEmpty()) {
                chatAdapter.updateData(groups)
            } else {
                showNoGroupsMessage()
            }
        }

        fetchGroups()

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

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onResume() {
        super.onResume()
        binding.rvChatGroups.visibility = View.VISIBLE
        binding.etSearch.visibility = View.VISIBLE
    }

    private fun fetchGroups() {
        binding.progressBar.visibility = View.VISIBLE

        firebaseManager.getAllCityGroups { citygroups ->
            binding.progressBar.visibility = View.GONE

            if (citygroups.isNotEmpty()) {
                groupViewModel.setGroups(citygroups)
            } else {
                showNoGroupsMessage()
            }
        }
    }

    private fun openGroupChat(group: CityGroups) {
        if (group.id.isNullOrEmpty() || group.title.isNullOrEmpty()) {
            Log.e("FragmentHomeChatt", "Group ID or Title is empty")
            Toast.makeText(requireContext(), "Group information is missing", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val currentUserId = currentUser.uid

            firebaseManager.getUserName(currentUserId) { userName ->
                val groupName = group.title ?: "Unknown Group"

              //  Log.d("GroupCheck", "Current User ID: $currentUserId")
              //  Log.d("GroupCheck", "Admin ID: ${group.adminId}")
               // Log.d("GroupCheck", "Group Members: ${group.members}")

                if (group.adminId.isNullOrEmpty() || group.members.isNullOrEmpty()) {
                    Log.e("GroupCheck", "Admin ID or Members list is empty!")
                    Toast.makeText(requireContext(), "Group data is incomplete", Toast.LENGTH_SHORT).show()
                    return@getUserName
                }

                val isAdmin = group.adminId == currentUserId
                val isMember = group.members.contains(currentUserId)

                if (isAdmin || isMember) {
                    val chatFragment = ChatFragment().apply {
                        arguments = Bundle().apply {
                            putString("currentUserId", currentUserId)
                            putString("GROUP_ID", group.id)
                            putString("GROUP_NAME", group.title)
                        }
                    }

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, chatFragment, ChatFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                } else {
                   // Log.e("GroupCheck", "User is NOT a member or admin!")
                    showJoinGroupDialog(group.id, currentUserId, userName, groupName)
                }
            }
        } else {
           // Log.e("FirebaseAuth", "User is not logged in")
            startActivity(Intent(requireContext(), LoginFragment::class.java))
            requireActivity().finish()
        }
    }

    // fun To request to join a group
    private fun showJoinGroupDialog(groupId: String, userId: String, userName: String, groupName: String) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Join Group")
            .setMessage("You are not a member of this group. Do you want to join?")
            .setPositiveButton("Yes") { _, _ ->

                firebaseManager.sendJoinRequest(groupId, userId, userName, groupName) { success ->
                    if (success) {
                        Toast.makeText(requireContext(), "Join request sent to admin", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to send join request", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun showNoGroupsMessage() {
        Toast.makeText(requireContext(), "No groups available", Toast.LENGTH_SHORT).show()
    }
}


