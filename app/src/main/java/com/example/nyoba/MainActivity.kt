package com.example.nyoba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        val email = findViewById<EditText>(R.id.inputEmail)
        val password = findViewById<EditText>(R.id.inputPassword)
        val btnMasuk = findViewById<Button>(R.id.btnMasuk)
        val txtDaftar = findViewById<TextView>(R.id.txtDaftar)

        btnMasuk.setOnClickListener {

            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()

            if (
                emailText.isEmpty() ||
                passwordText.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Email dan Password harus diisi",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                val checkUser = databaseHelper.checkUser(
                    emailText,
                    passwordText
                )

                if (checkUser) {

                    Toast.makeText(
                        this,
                        "Login berhasil",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Simpan email user yang login
                    val sharedPref =
                        getSharedPreferences(
                            "USER_SESSION",
                            MODE_PRIVATE
                        )

                    sharedPref.edit()
                        .putString(
                            "email",
                            emailText
                        )
                        .apply()

                    val intent = Intent(
                        this,
                        DashboardActivity::class.java
                    )

                    startActivity(intent)

                    finish()

                } else {

                    Toast.makeText(
                        this,
                        "Email atau Password salah",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        txtDaftar.setOnClickListener {

            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)

        }
    }
}