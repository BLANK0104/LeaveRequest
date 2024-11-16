package com.example.leavekotlin.loginandcreateuser

data class LeaveRequest(
    val studentId: Int,
    val startDate: String,
    val endDate: String,
    val reason: String
)