package com.example.grannar.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grannar.adapter.HomeScreenAdapter
import com.example.grannar.data.Calender.EventsData
import com.example.grannar.databinding.ActivityHomeBinding
import com.example.grannar.ui.viewmodel.UserStatusViewModel

class HomeActivity : AppCompatActivity() {

    //Recycler view i topp för kommande aktiviteter
    private var layoutManagerEvent: RecyclerView.LayoutManager? = null
    private var adapterEvent: RecyclerView.Adapter<HomeScreenAdapter.ViewHolder>? = null


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

        /* TESTING  skapar lite test data */

//        val testLista: List<EventsData> = EventsData.mockUpData()
//
//        showRecyclerviewTop(testLista)



    }

    fun showRecyclerviewTop(lista: List<EventsData>){
        layoutManagerEvent = LinearLayoutManager(this)
        binding.rvHomescreen1.layoutManager =layoutManagerEvent
        adapterEvent = HomeScreenAdapter(lista)
        binding.rvHomescreen1.adapter = adapterEvent


    }

}