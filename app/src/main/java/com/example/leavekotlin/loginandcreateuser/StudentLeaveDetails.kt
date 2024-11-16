package com.example.leavekotlin.models

data class StudentLeaveDetails(
    val id: Int,
    val studentId: Int,
    val startDate: String,
    val endDate: String,
    val reason: String,
    val createdAt: String,
    val faculty_status: String,
    val hod_status: String,
    val warden_status: String,
    val gatekeeper_status: String,
    val studentname: String
)