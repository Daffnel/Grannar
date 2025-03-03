package com.example.grannar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grannar.data.model.Group

class GroupViewModel : ViewModel() {
    private val _groupList = MutableLiveData<List<Group>>()
    val groupList: LiveData<List<Group>> get() = _groupList

    fun setGroups(groups: List<Group>) {
        _groupList.value = groups
    }
}
