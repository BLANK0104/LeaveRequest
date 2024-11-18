package com.example.leavekotlin.models

import java.io.Serializable


data class Historyfaculty(
    val start_date: String,
    val end_date: String,
    val reason: String,
    val total_attendance: String,
    val total_days: Int,
    val academic_days_leave: Int,
    val guardian_name: String,
    val guardian_contact: String,
    val guardian_email: String,
    val faculty_status: String,
    val name: String
) : Serializable