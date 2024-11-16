package com.example.leavekotlin.models

data class LeaveRequest(
    val student_id: Int,
    val name: String,
    val start_date: String,
    val end_date: String,
    val reason: String,
    val status: String
)