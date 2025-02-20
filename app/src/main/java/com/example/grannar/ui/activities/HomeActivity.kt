package com.example.grannar.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.grannar.R
import com.example.grannar.databinding.ActivityHomeBinding
import com.example.grannar.ui.fragment.CalendarFragment
import com.example.grannar.ui.fragment.ChatFragment
import com.example.grannar.ui.fragment.ProfileFragment
import com.example.grannar.ui.viewmodel.UserStatusViewModel

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
            binding.ibCalendar.setOnClickListener{
                useOfImageButton(CalendarActivity::class.java)
            }
            binding.ibChat.setOnClickListener{
                useOfImageButton(ChatFragment())
            }
            binding.ibHome.setOnClickListener{
                useOfImageButton(HomeActivity::class.java)
            }
           // binding.ibGroup.setOnClickListener{
           //     useOfImageButton(GroupFragment())
           // }
            binding.ibProfile.setOnClickListener{
                useOfImageButton(ProfileFragment())
            }
    }
    private fun useOfImageButton(destination: Any){
        when (destination) {
            is Fragment ->{
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, destination)
            .addToBackStack(null)
            .commit()
        } is Class<*> -> {
            val intent = Intent(this, destination)
            startActivity(intent)
        }        }
    }
}