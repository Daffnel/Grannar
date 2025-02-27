package com.example.grannar.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grannar.data.Groups.CityGroups
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityGroupsViewModel(private val repository: CityGroupRepository): ViewModel() {

    private val _groupsByCity = MutableLiveData<List<CityGroups>>()
    val groupsByCity: LiveData<List<CityGroups>> = _groupsByCity

    fun getGroupByCity(city: String){
        repository.getGroupByCity(city){ groups ->
            _groupsByCity.postValue(groups)
        }
    }


    private val _city= MutableLiveData<String?>()
    val city: MutableLiveData<String?> = _city

    fun getUserCity(){
        repository.getUserCity { cityName ->
           _city.postValue(cityName)
        }
        Log.d("!!!","City === ${city.value}")
    }


    fun joinGroup(groupId: String){
        if (repository.userId != null) {
            repository.joinGrupp(groupId, repository.userId)
        }
    }




   /* suspend fun getGroupsByCity(userCity: String): List<CityGroups> {
        return cityGroupRepository.getGroupByCity(userCity)
    }

    fun logout(context: Context) {
        cityGroupRepository.logout(context)
    }

    suspend fun getUserCity(): String? {
        return cityGroupRepository.getUserCity()
    }

    fun addNewGroup(title: String, moreInfo: String, city: String) {
        cityGroupRepository.addNewGroup(title, moreInfo, city)
    }*/

}

