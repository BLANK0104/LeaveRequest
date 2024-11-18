package com.example.leavekotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leavekotlin.models.Historyfaculty

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryFacultyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val history = intent.getSerializableExtra("HISTORY_DATA") as? List<Historyfaculty> ?: emptyList()
        Log.d("HistoryActivity", "Received history: $history")

        historyAdapter = HistoryFacultyAdapter(history)
        recyclerView.adapter = historyAdapter
    }
}