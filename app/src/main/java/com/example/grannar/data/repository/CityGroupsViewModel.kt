package com.example.grannar.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grannar.data.Groups.CityGroups
import com.example.grannar.data.firebase.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

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

    private val _myGroups = MutableLiveData<List<CityGroups>>()
    val mygroups: LiveData<List<CityGroups>> get() = _myGroups

    fun getGroupsWhenMember(){
        repository.getGroupsWhenMember { groups ->
            _myGroups.value = groups
        }
    }

    fun joinGroup(groupId: String){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null){
            repository.joinGroup(groupId, userId) { success ->
                if (success){
                    getGroupsWhenMember()
                }else {
                    Log.e("!!!", "Failed to add user to group")
                }
            }
        } else {
            Log.e("!!!", "No user logged in")
        }
    }

    fun leaveGroup(groupId: String) {
        repository.leaveGroup(groupId) { success ->
            if (success){
                getGroupsWhenMember()
            }
        }
    }

}

