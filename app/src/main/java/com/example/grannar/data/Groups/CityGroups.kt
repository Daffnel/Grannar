package com.example.grannar.data.Groups

data class CityGroups(
    val id: String = "",
    val title: String = "",
    val moreInfo: String = "",
    val name: String = "",
    val createdBy: String = "",
    val city: String = "",
    val adminId: String = "",
    val members: List<String> = emptyList(),

) {
}