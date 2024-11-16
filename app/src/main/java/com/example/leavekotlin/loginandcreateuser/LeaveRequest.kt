package com.example.leavekotlin.loginandcreateuser

data class LeaveRequest(
    val studentId: Int,
    val startDate: String,
    val endDate: String,
    val reason: String,
    val totalAttendance: String,
    val academicDaysLeave: String,
    val totalDays: String,
    val guardianName: String,
    val guardianContact: String,
    val guardianEmail: String,
)