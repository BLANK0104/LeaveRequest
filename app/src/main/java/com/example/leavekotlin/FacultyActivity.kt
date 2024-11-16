package com.example.leavekotlin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.example.leavekotlin.loginandcreateuser.FacultyAccept
import com.example.leavekotlin.loginandcreateuser.FacultyReject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale


class FacultyActivity : AppCompatActivity() {

    private lateinit var leaveRequestsContainer: LinearLayout
    private lateinit var tvGreeting: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hod)

        leaveRequestsContainer = findViewById(R.id.leaveRequestsContainer)
        tvGreeting = findViewById(R.id.tvGreeting)

        val userName = intent.getStringExtra("USER_NAME")
        tvGreeting.text = "Hello, $userName"

        fetchAllLeaveRequests()
    }

    private fun fetchAllLeaveRequests() {
        RetrofitClient.apiService.getAllLeaveRequests().enqueue(object : Callback<AllLeavesFetched> {
            override fun onResponse(call: Call<AllLeavesFetched>, response: Response<AllLeavesFetched>) {
                if (response.isSuccessful) {
                    val leaveRequests = response.body()?.leaveRequests ?: emptyList()
                    displayLeaveRequests(leaveRequests)
                } else {
                    Log.e("HodActivity", "Failed to fetch leave requests: ${response.errorBody()?.string()}")
                    Toast.makeText(this@FacultyActivity, "Failed to fetch leave requests", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AllLeavesFetched>, t: Throwable) {
                Log.e("HodActivity", "Network error", t)
                Toast.makeText(this@FacultyActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayLeaveRequests(leaveRequests: List<LeaveRequest>) {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        leaveRequestsContainer.removeAllViews()

        for (leaveRequest in leaveRequests) {
            if (leaveRequest.hod_status != "Pending" && leaveRequest.faculty_status == "Approved") continue

            val leaveRequestView = layoutInflater.inflate(R.layout.leave_request_item, leaveRequestsContainer, false)

            val tvStudentName = leaveRequestView.findViewById<TextView>(R.id.tvStudentName)
            val tvLeaveDates = leaveRequestView.findViewById<TextView>(R.id.tvLeaveDates)
            val tvReason = leaveRequestView.findViewById<TextView>(R.id.tvReason)
            val tvTotalAttendance = leaveRequestView.findViewById<TextView>(R.id.tvTotalAttendance)
            val tvGuardianDetails = leaveRequestView.findViewById<TextView>(R.id.tvGuardianDetails)
            val tvAcademicDaysLeave = leaveRequestView.findViewById<TextView>(R.id.tvAcademicDaysLeave)
            val tvTotalDays = leaveRequestView.findViewById<TextView>(R.id.tvTotalDays)
            val btnApprove = leaveRequestView.findViewById<Button>(R.id.btnApprove)
            val btnReject = leaveRequestView.findViewById<Button>(R.id.btnReject)
            val btnCallGuardian = leaveRequestView.findViewById<Button>(R.id.btnCallGuardian)

            val startDate = inputDateFormat.parse(leaveRequest.start_date)
            val endDate = inputDateFormat.parse(leaveRequest.end_date)

            val formattedStartDate = outputDateFormat.format(startDate)
            val formattedEndDate = outputDateFormat.format(endDate)

            tvStudentName.text = "Name: ${leaveRequest.student_name}"
            tvLeaveDates.text = "Start Date: $formattedStartDate\nEnd Date: $formattedEndDate"
            tvReason.text = "Reason: ${leaveRequest.reason}"
            tvTotalAttendance.text = "Total Attendance: ${leaveRequest.total_attendance}"
            tvGuardianDetails.text = "Guardian Name: ${leaveRequest.guardian_name}\nGuardian Contact: ${leaveRequest.guardian_contact}\nGuardian Email: ${leaveRequest.guardian_email}"
            tvAcademicDaysLeave.text = "Academic Days Leave: ${leaveRequest.academic_days_leave}"
            tvTotalDays.text = "Total Days: ${leaveRequest.total_days}"

            btnApprove.setOnClickListener {
                approveLeave(leaveRequest.student_id)
            }

            btnReject.setOnClickListener {
                rejectLeave(leaveRequest.student_id)
            }

            btnCallGuardian.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${leaveRequest.guardian_contact}")
                }
                startActivity(intent)
            }

            leaveRequestsContainer.addView(leaveRequestView)
        }
    }

    private fun approveLeave(leaveId: Int) {
        RetrofitClient.apiService.approveLeaveByFaculty(leaveId).enqueue(object : Callback<FacultyAccept> {
            override fun onResponse(call: Call<FacultyAccept>, response: Response<FacultyAccept>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@FacultyActivity, "Leave approved successfully", Toast.LENGTH_SHORT).show()
                    recreate() // Reload the activity
                } else {
                    Log.e("FacultyActivity", "Failed to approve leave: ${response.errorBody()?.string()}")
                    Toast.makeText(this@FacultyActivity, "Failed to approve leave", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FacultyAccept>, t: Throwable) {
                Log.e("FacultyActivity", "Network error", t)
                Toast.makeText(this@FacultyActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun rejectLeave(leaveId: Int) {
        RetrofitClient.apiService.rejectLeaveByFaculty(leaveId).enqueue(object : Callback<FacultyReject> {
            override fun onResponse(call: Call<FacultyReject>, response: Response<FacultyReject>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@FacultyActivity, "Leave rejected successfully", Toast.LENGTH_SHORT).show()
                    recreate() // Reload the activity
                } else {
                    Log.e("FacultyActivity", "Failed to reject leave: ${response.errorBody()?.string()}")
                    Toast.makeText(this@FacultyActivity, "Failed to reject leave", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FacultyReject>, t: Throwable) {
                Log.e("FacultyActivity", "Network error", t)
                Toast.makeText(this@FacultyActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}