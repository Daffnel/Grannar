package com.example.grannar.data.Groups

data class CityGroups(
    val id: String = "",
    val name: String = "",
    val createdBy: String = "",
    val members: List<String> = emptyList() // List w UID for members

) {
}