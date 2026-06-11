package com.example.nyoba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_account)

        val txtUserName =
            findViewById<TextView>(
                R.id.txtUserName
            )

        val txtUserEmail =
            findViewById<TextView>(
                R.id.txtUserEmail
            )

        val btnSignOut =
            findViewById<Button>(
                R.id.btnSignOut
            )

        val btnHome =
            findViewById<ImageButton>(
                R.id.btnHome
            )

        val btnSupport =
            findViewById<ImageButton>(
                R.id.btnSupport
            )

        val btnAccount =
            findViewById<ImageButton>(
                R.id.btnAccount
            )

        // Data sementara
        val sharedPref =
            getSharedPreferences(
                "USER_SESSION",
                MODE_PRIVATE
            )

        val email =
            sharedPref.getString(
                "email",
                ""
            ) ?: ""

        Toast.makeText(
            this,
            "Email Login = $email",
            Toast.LENGTH_LONG
        ).show()

        val databaseHelper =
            DatabaseHelper(this)

        val username =
            databaseHelper.getUserName(
                email
            )

        txtUserName.text =
            username

        txtUserEmail.text =
            email

        btnSignOut.setOnClickListener {

            val sharedPref =
                getSharedPreferences(
                    "USER_SESSION",
                    MODE_PRIVATE
                )

            sharedPref.edit().clear().apply()

            val intent =
                Intent(
                    this,
                    MainActivity::class.java
                )

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            finish()
        }

        btnHome.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    DashboardActivity::class.java
                )
            )
        }

        btnSupport.setOnClickListener {

            Toast.makeText(
                this,
                "Support Coming Soon",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnAccount.setOnClickListener {

            Toast.makeText(
                this,
                "Anda sedang berada di halaman Account",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}