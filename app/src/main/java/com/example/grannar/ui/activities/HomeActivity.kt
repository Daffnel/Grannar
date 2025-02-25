package com.example.grannar.ui.activities
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.grannar.R
import com.example.grannar.adapter.HomeScreenAdapter
import com.example.grannar.databinding.ActivityHomeBinding
import com.example.grannar.ui.activities.MainActivity
import com.example.grannar.ui.fragment.CalendarFragment
import com.example.grannar.ui.fragment.ChatFragment
import com.example.grannar.ui.fragment.GroupsFragment
import com.example.grannar.ui.fragment.ProfileFragment
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

        userStatusViewModel.userStatus.observe(this) { isLoggedIn ->
            if (!isLoggedIn) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.calendar_menu -> {
                    showCalendarFragment()
                    true
                }
                /*R.id.chat_menu -> {
                    showChatFragment()
                    true
                } */
                R.id.home_menu -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    true
                }
                /* R.id.group_menu -> {
                     showGroupFragment()
                     true
                 } */
                R.id.profile_menu -> {
                    showProfileFragment()
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.home_menu
        }
    }

    private fun showCalendarFragment() {
        val calendarFragment = CalendarFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, calendarFragment)
            .addToBackStack(null)
            .commit()
    }

    /* private fun showChatFragment() {
         val chatFragment = ChatFragment()
         supportFragmentManager.beginTransaction()
             .replace(R.id.main_frame_layout, chatFragment)
             .addToBackStack(null)
             .commit()
     } */

    /* private fun showGroupFragment() {
         val groupFragment = GroupsFragment()
         supportFragmentManager.beginTransaction()
             .replace(R.id.main_frame_layout, groupFragment)
             .addToBackStack(null)
             .commit()
     } */

    private fun showProfileFragment() {
        val profileFragment = ProfileFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, profileFragment)
            .addToBackStack(null)
            .commit()
    }

}
