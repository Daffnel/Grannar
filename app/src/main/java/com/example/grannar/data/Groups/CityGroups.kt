package com.example.grannar.data.Groups

data class CityGroups(
    val id: String = "",
    val title: String = "",
    val moreInfo: String = "",
    val name: String = "",
    val createdBy: String = "",
    val city: String = "",
    val members: List<String> = emptyList(),// List w UID for members
    //val date: String = "",   // for information  on when the group was created
) {
}