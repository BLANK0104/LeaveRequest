package com.example.leavekotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leavekotlin.models.Historyfaculty

class HistoryFacultyAdapter(private var history: List<Historyfaculty>) : RecyclerView.Adapter<HistoryFacultyAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvname: TextView = itemView.findViewById(R.id.tvname)
        val tvStartDate: TextView = itemView.findViewById(R.id.tvStartDate)
        val tvEndDate: TextView = itemView.findViewById(R.id.tvEndDate)
        val tvReason: TextView = itemView.findViewById(R.id.tvReason)
        val tvTotalAttendance: TextView = itemView.findViewById(R.id.tvTotalAttendance)
        val tvGuardianDetails: TextView = itemView.findViewById(R.id.tvGuardianDetails)
        val tvAcademicDaysLeave: TextView = itemView.findViewById(R.id.tvAcademicDaysLeave)
        val tvTotalDays: TextView = itemView.findViewById(R.id.tvTotalDays)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item_faculty, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = history[position]
        holder.tvname.text = "Name of Student: ${historyItem.name}"
        holder.tvStartDate.text = "Start Date: ${historyItem.start_date}"
        holder.tvEndDate.text = "End Date: ${historyItem.end_date}"
        holder.tvReason.text = "Reason: ${historyItem.reason}"
        holder.tvTotalAttendance.text = "Total Attendance: ${historyItem.total_attendance}"
        holder.tvGuardianDetails.text = "Guardian: ${historyItem.guardian_name}, Contact: ${historyItem.guardian_contact}, Email: ${historyItem.guardian_email}"
        holder.tvAcademicDaysLeave.text = "Academic Days Leave: ${historyItem.academic_days_leave}"
        holder.tvTotalDays.text = "Total Days: ${historyItem.total_days}"
    }

    override fun getItemCount(): Int = history.size

    fun updateHistory(newHistory: List<Historyfaculty>) {
        history = newHistory
        notifyDataSetChanged()
    }
}