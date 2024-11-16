// MainActivity.kt
package com.example.leavekotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnCreateUser: Button = findViewById(R.id.btnCreateUser)

        btnLogin.setOnClickListener {
            // Handle login button click
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        btnCreateUser.setOnClickListener {
            // Handle create new user button click
            val createUserIntent = Intent(this, RegisterActivity::class.java)
            startActivity(createUserIntent)
        }


    }
}