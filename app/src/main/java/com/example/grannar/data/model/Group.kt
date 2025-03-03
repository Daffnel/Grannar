package com.example.grannar.data.model

data class Group(
    val id: String = "",
    val title: String = "",
    val moreInfo: String = "",
    val name: String = "",
    val createdBy: String = "",
    val city: String = "",
    val members: List<String> = emptyList()
){
    constructor() : this("", "", "","")
}
