package com.example.leavekotlin

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.leavekotlin.loginandcreateuser.LeaveRequest
import java.util.Calendar
import java.util.concurrent.TimeUnit

class LeaveFormActivity : AppCompatActivity() {

    private var userId: Int = 0
    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var etTotalDays: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_form)

        userId = intent.getIntExtra("USER_ID", 0)
        Log.d("LeaveFormActivity", "Received userId: $userId")

        etStartDate = findViewById(R.id.etStartDate)
        etEndDate = findViewById(R.id.etEndDate)
        etTotalDays = findViewById(R.id.etTotalDays)
        val etReason = findViewById<EditText>(R.id.etReason)
        val etTotalAttendance = findViewById<EditText>(R.id.etTotalAttendance)
        val etGuardianName = findViewById<EditText>(R.id.etGuardianName)
        val etGuardianContact = findViewById<EditText>(R.id.etGuardianContact)
        val etGuardianEmail = findViewById<EditText>(R.id.etGuardianEmail)
        val etAcademicDaysLeave = findViewById<EditText>(R.id.etAcademicDaysLeave)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnSelectStartDate = findViewById<Button>(R.id.btnStartDate)
        val btnSelectEndDate = findViewById<Button>(R.id.btnEndDate)

        btnSelectStartDate.setOnClickListener {
            showDatePickerDialog { date ->
                etStartDate.setText(date)
                calculateTotalDays()
            }
        }

        btnSelectEndDate.setOnClickListener {
            showDatePickerDialog { date ->
                etEndDate.setText(date)
                calculateTotalDays()
            }
        }

        btnSubmit.setOnClickListener {
            val startDate = etStartDate.text.toString()
            val endDate = etEndDate.text.toString()
            val reason = etReason.text.toString()
            val totalAttendance = etTotalAttendance.text.toString()
            val guardianName = etGuardianName.text.toString()
            val guardianContact = etGuardianContact.text.toString()
            val guardianEmail = etGuardianEmail.text.toString()
            val academicDaysLeave = etAcademicDaysLeave.text.toString()
            val totalDays = etTotalDays.text.toString()

            if (startDate.isEmpty() || endDate.isEmpty() || reason.isEmpty() || totalAttendance.isEmpty() || guardianName.isEmpty() || guardianContact.isEmpty() || guardianEmail.isEmpty() || academicDaysLeave.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isEndDateValid(startDate, endDate)) {
                Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("LeaveFormActivity", "Submitting leave request with startDate: $startDate, endDate: $endDate, reason: $reason")

            val leaveRequest = LeaveRequest(userId, startDate, endDate, reason, totalAttendance, academicDaysLeave, totalDays, guardianName, guardianContact, guardianEmail)
            RetrofitClient.apiService.submitLeaveRequest(leaveRequest).enqueue(object : Callback<LeaveRequest> {
                override fun onResponse(call: Call<LeaveRequest>, response: Response<LeaveRequest>) {
                    if (response.isSuccessful) {
                        Log.d("LeaveFormActivity", "Leave request submitted successfully: ${response.body()}")
                        val builder = AlertDialog.Builder(this@LeaveFormActivity)
                        builder.setMessage("Leave request submitted successfully")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                val intent = Intent(this@LeaveFormActivity, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                        val alert = builder.create()
                        alert.show()
                    } else {
                        Log.d("LeaveFormActivity", "Failed to submit leave request: ${response.errorBody()?.string()}")
                        Toast.makeText(this@LeaveFormActivity, "Failed to submit leave request", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LeaveRequest>, t: Throwable) {
                    Log.d("LeaveFormActivity", "Network error: ${t.message}")
                    Toast.makeText(this@LeaveFormActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showDatePickerDialog(onDateSet: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            onDateSet(date)
        }, year, month, day)

        // Set the minimum date to today's date
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun isEndDateValid(startDate: String, endDate: String): Boolean {
        val start = startDate.split("-").map { it.toInt() }
        val end = endDate.split("-").map { it.toInt() }

        val startCalendar = Calendar.getInstance().apply {
            set(start[0], start[1] - 1, start[2])
        }
        val endCalendar = Calendar.getInstance().apply {
            set(end[0], end[1] - 1, end[2])
        }

        return !endCalendar.before(startCalendar)
    }

    private fun calculateTotalDays() {
        val startDate = etStartDate.text.toString()
        val endDate = etEndDate.text.toString()

        if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
            val start = startDate.split("-").map { it.toInt() }
            val end = endDate.split("-").map { it.toInt() }

            val startCalendar = Calendar.getInstance().apply {
                set(start[0], start[1] - 1, start[2])
            }
            val endCalendar = Calendar.getInstance().apply {
                set(end[0], end[1] - 1, end[2])
            }

            val diff = endCalendar.timeInMillis - startCalendar.timeInMillis
            val totalDays = TimeUnit.MILLISECONDS.toDays(diff).toInt() + 1
            etTotalDays.setText(totalDays.toString())
        }
    }
}