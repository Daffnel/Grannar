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
import com.example.grannar.databinding.FragmentLoginBinding
import com.example.grannar.ui.activities.HomeActivity
import com.example.grannar.ui.viewmodel.AuthViewModel
import com.example.grannar.ui.viewmodel.AuthViewModelFactory

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider(requireActivity(), AuthViewModelFactory())[AuthViewModel::class.java]

        val login = binding.ButtonLogin

        val gotToRegister = binding.buttonCreateAccount

        login.setOnClickListener {
            val email = binding.editTextLoginEmail.text.toString()
            val password = binding.editTextLoginPassword.text.toString()


            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            authViewModel.loginUser(email, password)


        }

        // Goes to the home activity if successful
        authViewModel.authResult.observe(viewLifecycleOwner) { (success, message) ->
            if (success){
                Toast.makeText(requireContext(), "$message!", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else{
                Toast.makeText(requireContext(), "$message!", Toast.LENGTH_SHORT).show()
            }
        }

        gotToRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

    }

}