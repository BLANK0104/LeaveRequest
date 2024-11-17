package com.example.leavekotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leavekotlin.models.HistoryStudent
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(private var history: List<HistoryStudent>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStartDate: TextView = itemView.findViewById(R.id.tvStartDate)
        val tvEndDate: TextView = itemView.findViewById(R.id.tvEndDate)
        val tvReason: TextView = itemView.findViewById(R.id.tvReason)
        val tvFacultyStatus: TextView = itemView.findViewById(R.id.tvFacultyStatus)
        val tvHodStatus: TextView = itemView.findViewById(R.id.tvHodStatus)
        val tvWardenStatus: TextView = itemView.findViewById(R.id.tvWardenStatus)
        val tvGatekeeperStatus: TextView = itemView.findViewById(R.id.tvGatekeeperStatus)
        val tvTotalAttendance: TextView = itemView.findViewById(R.id.tvTotalAttendance)
        val tvGuardianName: TextView = itemView.findViewById(R.id.tvGuardianName)
        val tvGuardianContact: TextView = itemView.findViewById(R.id.tvGuardianContact)
        val tvGuardianEmail: TextView = itemView.findViewById(R.id.tvGuardianEmail)
        val tvAcademicDaysLeave: TextView = itemView.findViewById(R.id.tvAcademicDaysLeave)
        val tvTotalDays: TextView = itemView.findViewById(R.id.tvTotalDays)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val leaveRequest = history[position]
        holder.tvStartDate.text = "Start Date: ${formatDate(leaveRequest.start_date)}"
        holder.tvEndDate.text = "End Date: ${formatDate(leaveRequest.end_date)}"
        holder.tvReason.text = "Reason: ${leaveRequest.reason}"
        holder.tvFacultyStatus.text = "Faculty Status: ${leaveRequest.faculty_status}"
        holder.tvHodStatus.text = "HOD Status: ${leaveRequest.hod_status}"
        holder.tvWardenStatus.text = "Warden Status: ${leaveRequest.warden_status}"
        holder.tvGatekeeperStatus.text = "Gatekeeper Status: ${leaveRequest.gatekeeper_status}"
        holder.tvTotalAttendance.text = "Total Attendance: ${leaveRequest.total_attendance}"
        holder.tvGuardianName.text = "Guardian Name: ${leaveRequest.guardian_name}"
        holder.tvGuardianContact.text = "Guardian Contact: ${leaveRequest.guardian_contact}"
        holder.tvGuardianEmail.text = "Guardian Email: ${leaveRequest.guardian_email}"
        holder.tvAcademicDaysLeave.text = "Academic Days Leave: ${leaveRequest.academic_days_leave}"
        holder.tvTotalDays.text = "Total Days: ${leaveRequest.total_days}"
    }

    override fun getItemCount(): Int = history.size

    fun updateHistory(newHistory: List<HistoryStudent>) {
        history = newHistory
        notifyDataSetChanged()
    }

    private fun formatDate(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateTime)
        return outputFormat.format(date)
    }
}