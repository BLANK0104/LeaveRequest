package com.example.leavekotlin.models

data class HistoryStudent (

        val student_id: Int,
        val name: String,
        val start_date: String,
        val end_date: String,
        val reason: String,
        val faculty_status: String,
        val hod_status: String,
        val warden_status: String,
        val gatekeeper_status: String,
        val total_attendance: String,
        val guardian_name: String,
        val guardian_contact: String,
        val guardian_email: String,
        val academic_days_leave: String,
        val total_days: String
)