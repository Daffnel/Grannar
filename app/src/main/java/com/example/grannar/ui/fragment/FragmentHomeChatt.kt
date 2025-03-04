package com.example.grannar.ui.fragment

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

        firebaseManager.getGroupsWhenMember { citygroups ->
            binding.progressBar.visibility = View.GONE

            if (citygroups.isNotEmpty()) {
                groupViewModel.setGroups(citygroups)
            } else {
                showNoGroupsMessage()
            }
        }
    }

    private fun openGroupChat(cityGroups: CityGroups) {
        if (cityGroups.id.isNullOrEmpty() || cityGroups.title.isNullOrEmpty()) {
            Log.e("FragmentHomeChatt", "Group ID or Title is empty")
            Toast.makeText(requireContext(), "Group information is missing", Toast.LENGTH_SHORT).show()
            return
        }


        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val currentUserId = currentUser.uid
            Log.d("FirebaseAuth", "Current User ID: $currentUserId")


            val chatFragment = ChatFragment().apply {
                arguments = Bundle().apply {
                    putString("currentUserId", currentUserId)
                    putString("GROUP_ID", cityGroups.id)
                    putString("GROUP_NAME", cityGroups.title)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, chatFragment, ChatFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
        } else {
            Log.e("FirebaseAuth", "User is not logged in")

            startActivity(Intent(requireContext(),LoginFragment::class.java))
            requireActivity().finish()
        }
    }

    private fun showNoGroupsMessage() {
        Toast.makeText(requireContext(), "No groups available", Toast.LENGTH_SHORT).show()
    }
}


