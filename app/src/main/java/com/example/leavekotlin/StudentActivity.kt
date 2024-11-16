package com.example.leavekotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.leavekotlin.models.StudentLeaveDetails
import com.example.leavekotlin.models.LeaveRequestsResponse
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class StudentActivity : AppCompatActivity() {

    private var userId: Int = 0
    private var username: String = ""
    private lateinit var tvLeaveRequests: TextView
    private lateinit var tvGreeting: TextView
    private lateinit var btnLeaveForm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        userId = intent.getIntExtra("USER_ID", 0)
        username = intent.getStringExtra("USER_NAME") ?: ""
        Log.d("StudentActivity", "Received userId: $userId and username: $username")

        tvLeaveRequests = findViewById(R.id.tvLeaveRequests)
        tvGreeting = findViewById(R.id.tvGreeting)
        btnLeaveForm = findViewById(R.id.btnLeaveForm)

        tvGreeting.text = "Hello, $username"

        fetchLeaveRequests()

        btnLeaveForm.setOnClickListener {
            if (btnLeaveForm.isEnabled) {
                val intent = Intent(this@StudentActivity, LeaveFormActivity::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
            } else {
                showAlert()
            }
        }
    }

    private fun fetchLeaveRequests() {
        RetrofitClient.apiService.getLeaveRequests(userId).enqueue(object : Callback<LeaveRequestsResponse> {
            override fun onResponse(call: Call<LeaveRequestsResponse>, response: Response<LeaveRequestsResponse>) {
                if (response.isSuccessful) {
                    val leaveRequests = response.body()?.leaveRequests ?: emptyList()
                    displayLeaveRequests(leaveRequests)
                } else {
                    Log.d("StudentActivity", "Failed to fetch leave requests: ${response.errorBody()?.string()}")
                    Toast.makeText(this@StudentActivity, "Failed to fetch leave requests", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LeaveRequestsResponse>, t: Throwable) {
                Log.d("StudentActivity", "Network error: ${t.message}")
                Toast.makeText(this@StudentActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayLeaveRequests(leaveRequests: List<StudentLeaveDetails>) {
        var hasPendingRequest = false
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val leaveRequestsText = leaveRequests.joinToString(separator = "\n") { request ->
            val startDate = request.startDate?.let { dateFormat.parse(it) }
            val endDate = request.endDate?.let { dateFormat.parse(it) }
            val diffInDays = if (startDate != null && endDate != null) {
                val diffInMillies = endDate.time - startDate.time
                TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
            } else {
                null
            }

            when {
                request.faculty_status == "Pending" -> {
                    hasPendingRequest = true
                    "Leave Decision pending from faculty"
                }
                request.faculty_status == "Approved" && request.hod_status == "Pending" -> {
                    hasPendingRequest = true
                    "Leave Approved from faculty and waiting for approval from HOD"
                }
                diffInDays != null && diffInDays < 3 && request.warden_status == "Pending" -> {
                    hasPendingRequest = true
                    "You may go to warden for approval"
                }
                request.warden_status == "Approved" && request.gatekeeper_status == "Pending" -> {
                    hasPendingRequest = true
                    "You may go to the gate to finalize your leave"
                }
                request.gatekeeper_status == "Approved" -> "Leave has been granted successfully"
                else -> "Leave request status unknown"
            }
        }

        if (leaveRequests.isEmpty()) {
            tvLeaveRequests.text = "Eligible to request a leave"
            btnLeaveForm.isEnabled = true
        } else {
            tvLeaveRequests.text = leaveRequestsText
            btnLeaveForm.isEnabled = !hasPendingRequest
        }
    }

    private fun showAlert() {
        AlertDialog.Builder(this)
            .setTitle("Alert")
            .setMessage("You can submit only a single leave request at once.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}