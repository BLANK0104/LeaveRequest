package com.example.leavekotlin.loginandcreateuser
data class CurrentUserResponse(
    val id: Int,
    val username: String,
    val role: String
)