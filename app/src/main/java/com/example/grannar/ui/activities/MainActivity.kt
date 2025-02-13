package com.example.grannar.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.grannar.R
import com.example.grannar.databinding.ActivityMainBinding
import com.example.grannar.ui.viewmodel.AuthViewModel
import com.example.grannar.ui.viewmodel.AuthViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        authViewModel = ViewModelProvider(this, AuthViewModelFactory())[AuthViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signup = binding.buttonCreateAccount
        val login = binding.ButtonLogin

        signup.setOnClickListener {
            val email = binding.editTextLoginName.text.toString()
            val password = binding.editTextLoginPassword.text.toString()
            authViewModel.registerUser(email, password)
        }

        login.setOnClickListener {
            val email = binding.editTextLoginName.text.toString()
            val password = binding.editTextLoginPassword.text.toString()
            authViewModel.loginUser(email, password)
        }

        authViewModel.authResult.observe(this) { (success, message) ->
            if (success){
                Toast.makeText(this, "$message!", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this, "$message!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}