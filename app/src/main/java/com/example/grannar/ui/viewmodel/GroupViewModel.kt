package com.example.grannar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grannar.data.Groups.CityGroups
import com.example.grannar.data.model.Group

class GroupViewModel : ViewModel() {
    private val _groupList = MutableLiveData<List<CityGroups>>()
    val groupList: LiveData<List<CityGroups>> get() = _groupList

    fun setGroups(groups: List<CityGroups>) {
        _groupList.value = groups
    }
}
