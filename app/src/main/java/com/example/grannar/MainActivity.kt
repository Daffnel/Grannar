package com.example.grannar

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.grannar.data.firebase.FirebaseManager
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

        val signup = binding.ButtonCreateAccount

        signup.setOnClickListener {
            createAccount()
        }

    }

    private fun createAccount(){
        val email = binding.EditTextLoginName.text.toString()
        val password = binding.EditTextLoginPassword.text.toString()
        authViewModel.registerUser(email, password)
        Log.e("!!!", "$email")
    }
}