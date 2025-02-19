package com.example.grannar.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.grannar.R
import com.example.grannar.databinding.ActivityHomeBinding
import com.example.grannar.ui.viewmodel.UserStatusViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userStatusViewModel: UserStatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userStatusViewModel = ViewModelProvider(this).get(UserStatusViewModel::class.java)

        //checking to see if user is logged in, if not send back to login page
        userStatusViewModel.userStatus.observe(this) { isLoggedIn ->
            if (!isLoggedIn) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

    }
}