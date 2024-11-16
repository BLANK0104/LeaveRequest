// LoginActivity.kt
package com.example.leavekotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.leavekotlin.loginandcreateuser.LoginRequest
import com.example.leavekotlin.loginandcreateuser.CurrentRoleandUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val logo = findViewById<ImageView>(R.id.logo)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnCreateNewUser = findViewById<Button>(R.id.btnCreateUser)

        // Animate logo
        logo.alpha = 0f
        logo.animate()
            .alpha(1f)
            .setDuration(1000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // Animate EditTexts and Buttons
        val views = arrayOf(etUsername, etPassword, btnLogin, btnCreateNewUser)
        for (view in views) {
            view.translationY = 100f
            view.alpha = 0f
            view.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(1000)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            Log.d("LoginActivity", "Sending login request with username: $username and password: $password")

            val loginRequest = LoginRequest(username, password)
            RetrofitClient.apiService.login(loginRequest).enqueue(object : Callback<CurrentRoleandUserResponse> {
                override fun onResponse(call: Call<CurrentRoleandUserResponse>, response: Response<CurrentRoleandUserResponse>) {
                    if (response.isSuccessful) {
                        val currentRoleandUserResponse = response.body()
                        if (currentRoleandUserResponse != null) {
                            navigateToRoleActivity(currentRoleandUserResponse)
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("LoginActivity", "Login failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CurrentRoleandUserResponse>, t: Throwable) {
                    Log.e("LoginActivity", "Network error", t)
                    Toast.makeText(this@LoginActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnCreateNewUser.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun navigateToRoleActivity(response: CurrentRoleandUserResponse) {
        val id = response.user.id
        val name = response.user.name
        val role = response.user.role
        Log.d("LoginActivity", "Received response with name: $name, role: $role, and id: $id")
        Toast.makeText(this@LoginActivity, "Welcome $name", Toast.LENGTH_SHORT).show()
        when (role) {
            "Student" -> {
                val intent = Intent(this@LoginActivity, StudentActivity::class.java)
                intent.putExtra("USER_ID", id)
                intent.putExtra("USER_NAME", name)
                intent.putExtra("USER_ROLE", role)
                Log.d("LoginActivity", "Navigating to StudentActivity with id: $id and name: $name")
                startActivity(intent)
            }

            "Faculty" -> {
                val intent = Intent(this@LoginActivity, FacultyActivity::class.java)
                intent.putExtra("USER_ID", id)
                intent.putExtra("USER_NAME", name)
                intent.putExtra("USER_ROLE", role)
                Log.d("LoginActivity", "Navigating to AcceptRejectActivity with id: $id and name: $name")
                startActivity(intent)
            }

             "HOD" -> {
                val intent = Intent(this@LoginActivity, HodActivity::class.java)
                intent.putExtra("USER_ID", id)
                intent.putExtra("USER_NAME", name)
                intent.putExtra("USER_ROLE", role)
                Log.d("LoginActivity", "Navigating to AcceptRejectActivity with id: $id and name: $name")
                startActivity(intent)
            }

             "Gatekeeper" -> {
                val intent = Intent(this@LoginActivity, GatekeeperActivity::class.java)
                intent.putExtra("USER_ID", id)
                intent.putExtra("USER_NAME", name)
                intent.putExtra("USER_ROLE", role)
                Log.d("LoginActivity", "Navigating to AcceptActivity with id: $id and name: $name")
                startActivity(intent)
            }

            "Warden" -> {
                val intent = Intent(this@LoginActivity, WardenActivity::class.java)
                intent.putExtra("USER_ID", id)
                intent.putExtra("USER_NAME", name)
                intent.putExtra("USER_ROLE", role)
                Log.d("LoginActivity", "Navigating to AcceptActivity with id: $id and name: $name")
                startActivity(intent)
            }

            else -> {
                Log.d("LoginActivity", "Unknown role: $role")
                Toast.makeText(this@LoginActivity, "Unknown role", Toast.LENGTH_SHORT).show()
            }
        }
    }
}