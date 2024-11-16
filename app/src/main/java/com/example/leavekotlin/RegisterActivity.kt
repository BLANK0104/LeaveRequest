package com.example.leavekotlin

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.leavekotlin.loginandcreateuser.CreateUserRequest
import com.example.leavekotlin.loginandcreateuser.CreateUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etName)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val spinnerRole = findViewById<Spinner>(R.id.spinnerRole)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etRePassword = findViewById<EditText>(R.id.etRePassword)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        // Populate the Spinner with role options
        val roles = arrayOf("Student", "Faculty", "HOD", "Warden", "Gatekeeper")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter

        btnSubmit.setOnClickListener {
            val name = etName.text.toString()
            val username = etUsername.text.toString()
            val role = spinnerRole.selectedItem.toString()
            val password = etPassword.text.toString()
            val rePassword = etRePassword.text.toString()

            if (password == rePassword) {
                val createUserRequest = CreateUserRequest(name, username, role, password)
                RetrofitClient.apiService.createUser(createUserRequest).enqueue(object : Callback<CreateUserResponse> {
                    override fun onResponse(call: Call<CreateUserResponse>, response: Response<CreateUserResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@RegisterActivity, "User created successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Failed to create user", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<CreateUserResponse>, t: Throwable) {
                        Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}