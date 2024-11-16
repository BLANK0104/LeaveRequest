package com.example.leavekotlin.models

data class LeaveRequestsResponseHod(
    val student_id: Int,
    val id: Int,
    val start_date: String,
    val end_date: String,
    val reason: String,
    val name: String,
    val status: String,
    val message: String
)