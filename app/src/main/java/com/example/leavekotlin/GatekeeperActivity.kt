package com.example.leavekotlin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.leavekotlin.loginandcreateuser.GatekeeperAccept
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class GatekeeperActivity : AppCompatActivity() {

    private lateinit var leaveRequestsContainer: LinearLayout
    private lateinit var tvGreeting: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gatekeeper)

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
                    Log.e("GatekeeperActivity", "Failed to fetch leave requests: ${response.errorBody()?.string()}")
                    Toast.makeText(this@GatekeeperActivity, "Failed to fetch leave requests", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AllLeavesFetched>, t: Throwable) {
                Log.e("GatekeeperActivity", "Network error", t)
                Toast.makeText(this@GatekeeperActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayLeaveRequests(leaveRequests: List<LeaveRequest>) {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        leaveRequestsContainer.removeAllViews()
        for (leaveRequest in leaveRequests) {
            if (leaveRequest.gatekeeper_status != "Pending" && leaveRequest.warden_status != "Approved" && leaveRequest.hod_status != "Aproved" && leaveRequest.faculty_status == "Approved") continue

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
        RetrofitClient.apiService.approveLeaveByGatekeeper(leaveId).enqueue(object : Callback<GatekeeperAccept> {
            override fun onResponse(call: Call<GatekeeperAccept>, response: Response<GatekeeperAccept>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@GatekeeperActivity, "Leave approved successfully", Toast.LENGTH_SHORT).show()
                    recreate() // Reload the activity
                } else {
                    Log.e("GatekeeperActivity", "Failed to approve leave: ${response.errorBody()?.string()}")
                    Toast.makeText(this@GatekeeperActivity, "Failed to approve leave", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GatekeeperAccept>, t: Throwable) {
                Log.e("GatekeeperActivity", "Network error", t)
                Toast.makeText(this@GatekeeperActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}