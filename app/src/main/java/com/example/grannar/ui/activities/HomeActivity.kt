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
import com.example.grannar.data.Calender.EventsData
import com.example.grannar.data.firebase.FirebaseManager
import com.example.grannar.data.repository.EventsRepository
import com.example.grannar.databinding.ActivityHomeBinding
import com.example.grannar.ui.fragment.CalendarFragment
import com.example.grannar.ui.fragment.ChatFragment
import com.example.grannar.ui.fragment.FragmentHomeChatt
import com.example.grannar.ui.fragment.GroupFragment

import com.example.grannar.ui.fragment.LoginFragment
//import com.example.grannar.ui.fragment.GroupsFragment

import com.example.grannar.ui.fragment.ProfileFragment
import com.example.grannar.ui.viewmodel.CalendarViewModel
import com.example.grannar.ui.viewmodel.UserStatusViewModel
import java.time.LocalDate

class HomeActivity : AppCompatActivity(), CalenderEventAdapter.OnItemClickListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userStatusViewModel: UserStatusViewModel
    private lateinit var viewModel: CalendarViewModel
    private lateinit var layoutManagerEvents: LinearLayoutManager
    private lateinit var adapterEvents: CalenderEventAdapter
    private val eventsRepository = EventsRepository()
    private val firebaseManager = FirebaseManager()

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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.show()

        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.calendar_menu -> {
                    showCalendarFragment()
                    true
                }
                R.id.chat_menu -> {
                    showChatFragment()
                    true
                }
                R.id.home_menu -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    binding.rvHomescreen1.visibility = View.VISIBLE
                    binding.rvHomescreen3.visibility = View.VISIBLE
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


        binding.logoutButton.setOnClickListener {
            logoutUser()
        }


    }

    private fun showCalendarFragment() {
        binding.rvHomescreen1.visibility=View.GONE
        binding.rvHomescreen3.visibility=View.GONE
        val calendarFragment = CalendarFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, calendarFragment)
            .addToBackStack(null)
            .commit()
    }


    private fun showChatFragment() {
        val fragment = supportFragmentManager.findFragmentByTag(FragmentHomeChatt::class.java.simpleName)
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FragmentHomeChatt())
                .addToBackStack(null)
                .commit()
        }
    }

     private fun showGroupFragment() {

         binding.rvHomescreen1.visibility=View.GONE
         binding.rvHomescreen3.visibility=View.GONE
         val groupFragment = GroupFragment()
         supportFragmentManager.beginTransaction()
             .replace(R.id.container, groupFragment)
             .addToBackStack(null)
             .commit()
     }

    private fun showProfileFragment() {
        binding.rvHomescreen1.visibility=View.GONE
        binding.rvHomescreen3.visibility=View.GONE
        val profileFragment = ProfileFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, profileFragment)
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

    private fun logoutUser() {
        firebaseManager.logout()
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}
