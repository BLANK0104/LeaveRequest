// CurrentRoleandUserResponse.kt
package com.example.leavekotlin.loginandcreateuser

data class CurrentRoleandUserResponse(
    val message: String,
    val user: User
)

data class User(
    val id: Int,
    val name: String,
    val role: String,
    val username: String,
    val password: String
)