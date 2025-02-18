package com.example.grannar.data.model

data class User (
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val age: Int = 0,
    val city: String = "",
    val bio: String = "",
    val interests: List<String> = emptyList()
)