package com.example.grannar.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.grannar.R
import com.example.grannar.databinding.FragmentRegisterBinding
import com.example.grannar.ui.activities.HomeActivity
import com.example.grannar.ui.viewmodel.AuthViewModel
import com.example.grannar.ui.viewmodel.AuthViewModelFactory

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(this, AuthViewModelFactory())[AuthViewModel::class.java]

        //Register when clicking next
        binding.buttonNext.setOnClickListener {
            val email = binding.editTextRegisterName.text.toString()
            val password = binding.EditTextRegisterPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.registerUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigates back to Login
        binding.buttonBackRegister.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        authViewModel.authResult.observe(viewLifecycleOwner) { (success, message) ->
            if (success){
                Toast.makeText(requireContext(), "$message!", Toast.LENGTH_SHORT).show()
               parentFragmentManager.beginTransaction()
                   .replace(R.id.main, ProfileFragment())
                   .addToBackStack(null)
                   .commit()
            } else{
                Toast.makeText(requireContext(), "$message!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
