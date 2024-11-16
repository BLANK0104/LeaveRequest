package com.example.leavekotlin.loginandcreateuser

data class CreateUserResponse(
    val id: Int,
    val username: String,
    val role: String
)