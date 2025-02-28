package com.example.grannar.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grannar.data.Groups.CityGroups
import com.example.grannar.data.firebase.FirebaseManager

class CityGroupsViewModel(private val repository: FirebaseManager): ViewModel() {

   // lateinit var fireBaseManager: FirebaseManager


    private val _userCity = MutableLiveData<String>()
    val userCity: LiveData<String> get() = _userCity

    fun getUserCity(){
        Log.d("!!!","get user Anropad")
        repository.getUserCity { city ->
            _userCity.postValue(city ?: "Hittar ingen stad")
        }
    }

    fun addNewGroup(title: String, moreInfo: String,city: String){

        repository.addNewGroup(title,moreInfo,city)
    }

    private val _groups = MutableLiveData<List<CityGroups>>()
    val groups: LiveData<List<CityGroups>> get() = _groups

    fun getGroupByCity(){

        repository.getGroupsByUsersCity{ groups ->
            _groups.value = groups

        }
    }

}

