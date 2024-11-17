package com.example.leavekotlin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leavekotlin.models.HistoryResponse
import com.example.leavekotlin.models.LeaveRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryStudentActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_student)

        userId = intent.getIntExtra("USER_ID", 0)
        recyclerView = findViewById(R.id.recyclerViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryAdapter(emptyList())
        recyclerView.adapter = historyAdapter

        fetchStudentHistory()
    }

    private fun fetchStudentHistory() {
        RetrofitClient.apiService.getStudentHistory(userId).enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful) {
                    val history = response.body()?.history ?: emptyList()
                    historyAdapter.updateHistory(history)
                } else {
                    Log.d("HistoryStudentActivity", "Failed to fetch history: ${response.errorBody()?.string()}")
                    Toast.makeText(this@HistoryStudentActivity, "Failed to fetch history", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                Log.d("HistoryStudentActivity", "Network error: ${t.message}")
                Toast.makeText(this@HistoryStudentActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}