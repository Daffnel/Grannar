package com.example.grannar.data.repository

import android.content.Context
import com.example.grannar.data.Groups.CityGroups
import com.example.grannar.data.firebase.GroupFirebaseManager
import com.example.grannar.data.model.Group

class GroupRepository(private val groupFirebaseManager: GroupFirebaseManager) {

    suspend fun getGroupsByCity(userCity: String): List<CityGroups> {
        return groupFirebaseManager.getGroupByCity(userCity)
    }

    fun logout(context: Context) {
        groupFirebaseManager.logout(context)
    }

    suspend fun getUserCity(): String? {
        return groupFirebaseManager.getUserCity()
    }

    fun addNewGroup(title: String, moreInfo: String, city: String) {
        groupFirebaseManager.addNewGroup(title, moreInfo, city)
    }

}

