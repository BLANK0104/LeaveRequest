// AllLeavesFetched.kt
package com.example.leavekotlin

data class AllLeavesFetched(
    val leaveRequests: List<LeaveRequest>
)

data class LeaveRequest(
    val id: Int,
    val student_id: Int,
    val start_date: String,
    val end_date: String,
    val reason: String,
    val created_at: String,
    val faculty_status: String,
    val hod_status: String,
    val warden_status: String,
    val gatekeeper_status: String,
    val student_name: String
)