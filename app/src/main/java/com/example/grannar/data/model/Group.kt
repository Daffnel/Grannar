package com.example.grannar.data.model

data class Group(
    val groupId: String = "",
    val groupName: String = "",
    val creatorId: String = "",
    val members: List<String> = emptyList()
)