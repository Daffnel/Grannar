package com.example.grannar.ui.activities
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grannar.R
import com.example.grannar.adapter.CalenderEventAdapter
import com.example.grannar.adapter.HomeScreenAdapter
import com.example.grannar.data.Calender.EventsData
import com.example.grannar.data.repository.EventsRepository
import com.example.grannar.databinding.ActivityHomeBinding
import com.example.grannar.ui.activities.MainActivity
import com.example.grannar.ui.fragment.CalendarFragment
import com.example.grannar.ui.fragment.ChatFragment
import com.example.grannar.ui.fragment.GroupFragment
//import com.example.grannar.ui.fragment.GroupsFragment
import com.example.grannar.ui.fragment.ProfileFragment
import com.example.grannar.ui.viewmodel.CalendarViewModel
import com.example.grannar.ui.viewmodel.UserStatusViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate

class HomeActivity : AppCompatActivity(), CalenderEventAdapter.OnItemClickListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userStatusViewModel: UserStatusViewModel
    private lateinit var viewModel: CalendarViewModel
    private lateinit var layoutManagerEvents: LinearLayoutManager
    private lateinit var adapterEvents: CalenderEventAdapter
    private val eventsRepository = EventsRepository()

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
        viewModel = ViewModelProvider(this, com.example.grannar.ui.viewmodel.CalenderViewModelFactory(eventsRepository)
        )[CalendarViewModel::class.java]
        viewModel.events.observe(this){ eventList ->
            adapterEvents.updateData(eventList)
        }

        viewModel.getEvents(LocalDate.now().year, LocalDate.now().monthValue)

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
                R.id.group_menu -> {
                     showGroupFragment()
                     true
                 }
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

        eventHomeScreen()

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

     private fun showGroupFragment() {
         val groupFragment = GroupFragment()
         supportFragmentManager.beginTransaction()
             .replace(R.id.main_frame_layout, groupFragment)
             .addToBackStack(null)
             .commit()
     }

    private fun showProfileFragment() {
        val profileFragment = ProfileFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, profileFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun eventHomeScreen(){
        layoutManagerEvents = LinearLayoutManager(this)
        binding.rvHomescreen3.layoutManager = layoutManagerEvents
        adapterEvents = CalenderEventAdapter(emptyList(), this)
        binding.rvHomescreen3.adapter = adapterEvents
    }

    override fun showPopUpDialog(events: EventsData) {
    }

}
