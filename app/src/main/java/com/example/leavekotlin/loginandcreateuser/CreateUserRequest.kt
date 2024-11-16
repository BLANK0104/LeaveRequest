package com.example.leavekotlin.loginandcreateuser

data class CreateUserRequest(
    val name: String,
    val username: String,
    val role: String,
    val password: String
)