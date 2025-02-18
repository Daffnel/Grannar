package com.example.grannar.ui.fragment

import android.os.Bundle
import android.provider.ContactsContract.Profile
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.grannar.R
import com.example.grannar.data.firebase.FirebaseManager
import com.example.grannar.databinding.FragmentLoginBinding
import com.example.grannar.databinding.FragmentProfileBinding
import com.example.grannar.ui.viewmodel.ProfileViewModel
import com.example.grannar.ui.viewmodel.ProfileViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val firebaseManager = FirebaseManager()

        viewModel = ViewModelProvider(this, ProfileViewModelFactory(firebaseManager)).get(ProfileViewModel::class.java)

        viewModel.getUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner){ user ->
            user?.let {
                binding.editTextName.setText(it.name)
                binding.editTextAge.setText(it.age.toString())
                binding.editTextCity.setText(it.city)
                binding.editTextBio.setText(it.bio)
                binding.editTextInterest.setText(it.interests.joinToString(", "))
            }
        }

        viewModel.updateStatus.observe(viewLifecycleOwner){ (success, message) ->
            if (success){
                Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
            }
        }




        binding.btnSave.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val age = binding.editTextAge.text.toString().toIntOrNull() ?: 0
            val city = binding.editTextCity.text.toString()
            val bio = binding.editTextBio.text.toString()
            val interest = binding.editTextInterest.text.toString().split(",").map { it.trim() }

            viewModel.updateUserProfile(name, age, city, bio, interest)
        }

        return binding.root
    }



}