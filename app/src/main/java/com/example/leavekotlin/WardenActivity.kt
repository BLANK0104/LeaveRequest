package com.example.leavekotlin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.leavekotlin.loginandcreateuser.WardenAccept
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class WardenActivity : AppCompatActivity() {

    private lateinit var leaveRequestsContainer: LinearLayout
    private lateinit var tvGreeting: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warden)

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
                    Log.e("WardenActivity", "Failed to fetch leave requests: ${response.errorBody()?.string()}")
                    Toast.makeText(this@WardenActivity, "Failed to fetch leave requests", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AllLeavesFetched>, t: Throwable) {
                Log.e("WardenActivity", "Network error", t)
                Toast.makeText(this@WardenActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayLeaveRequests(leaveRequests: List<LeaveRequest>) {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        leaveRequestsContainer.removeAllViews()

        for (leaveRequest in leaveRequests) {
            if (leaveRequest.warden_status != "Pending" && leaveRequest.hod_status != "Approved" && leaveRequest.faculty_status == "Approved") continue

            val leaveRequestView = layoutInflater.inflate(R.layout.leave_request_warden_gatekeeper, leaveRequestsContainer, false)

            val tvLeaveDetails = leaveRequestView.findViewById<TextView>(R.id.tvLeaveDetails)
            val btnApprove = leaveRequestView.findViewById<Button>(R.id.btnApprove)

            val startDate = inputDateFormat.parse(leaveRequest.start_date)
            val endDate = inputDateFormat.parse(leaveRequest.end_date)

            val formattedStartDate = outputDateFormat.format(startDate)
            val formattedEndDate = outputDateFormat.format(endDate)

            tvLeaveDetails.text = "Name: ${leaveRequest.student_name}\nStart Date: $formattedStartDate\nEnd Date: $formattedEndDate\nReason: ${leaveRequest.reason}"

            btnApprove.setOnClickListener {
                approveLeave(leaveRequest.student_id)
            }

            leaveRequestsContainer.addView(leaveRequestView)
        }
    }

    private fun approveLeave(leaveId: Int) {
        RetrofitClient.apiService.approveLeaveByWarden(leaveId).enqueue(object : Callback<WardenAccept> {
            override fun onResponse(call: Call<WardenAccept>, response: Response<WardenAccept>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@WardenActivity, "Leave approved successfully", Toast.LENGTH_SHORT).show()
                    recreate() // Reload the activity
                } else {
                    Log.e("WardenActivity", "Failed to approve leave: ${response.errorBody()?.string()}")
                    Toast.makeText(this@WardenActivity, "Failed to approve leave", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WardenAccept>, t: Throwable) {
                Log.e("WardenActivity", "Network error", t)
                Toast.makeText(this@WardenActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}