package com.example.leavekotlin

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AcceptActivity : AppCompatActivity() {

    private lateinit var tvLeaveRequests: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept)

        tvLeaveRequests = findViewById(R.id.tvLeaveRequests)
        fetchAllLeaveRequests()
    }

    private fun fetchAllLeaveRequests() {
        RetrofitClient.apiService.getAllLeaveRequests().enqueue(object : Callback<AllLeavesFetched> {
            override fun onResponse(call: Call<AllLeavesFetched>, response: Response<AllLeavesFetched>) {
                if (response.isSuccessful) {
                    val leaveRequests = response.body()
                    // Handle the leave requests and display them
                    tvLeaveRequests.text = leaveRequests.toString()
                    Log.d("AcceptActivity", "Fetched leave requests: $leaveRequests")
                } else {
                    Log.e("AcceptActivity", "Failed to fetch leave requests: ${response.errorBody()?.string()}")
                    Toast.makeText(this@AcceptActivity, "Failed to fetch leave requests", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AllLeavesFetched>, t: Throwable) {
                Log.e("AcceptActivity", "Network error", t)
                Toast.makeText(this@AcceptActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}