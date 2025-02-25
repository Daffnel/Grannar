package com.example.grannar.data.Groups

data class CityGroups(
    val id: String = "",
    val name: String = "",
    val createdBy: String = "",
    val members: List<String> = emptyList() // Lista med UID för medlemmar

) {
}