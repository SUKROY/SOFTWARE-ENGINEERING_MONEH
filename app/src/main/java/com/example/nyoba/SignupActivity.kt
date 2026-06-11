package com.example.nyoba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Patterns

class SignupActivity : AppCompatActivity() {

    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        databaseHelper = DatabaseHelper(this)

        val nama = findViewById<EditText>(R.id.inputNama)
        val email = findViewById<EditText>(R.id.inputEmailSignup)
        val password = findViewById<EditText>(R.id.inputPasswordSignup)
        val konfirmasi = findViewById<EditText>(R.id.inputKonfirmasiPassword)
        val btnDaftar = findViewById<Button>(R.id.btnDaftar)
        val txtMasuk = findViewById<TextView>(R.id.txtMasuk)

        btnDaftar.setOnClickListener {

            val namaText = nama.text.toString().trim()
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()
            val konfirmasiText = konfirmasi.text.toString().trim()

            if (
                namaText.isEmpty() ||
                emailText.isEmpty() ||
                passwordText.isEmpty() ||
                konfirmasiText.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Semua data harus diisi",
                    Toast.LENGTH_SHORT
                ).show()

            }
            else if (
                !Patterns.EMAIL_ADDRESS
                    .matcher(emailText)
                    .matches()
            ) {

                Toast.makeText(
                    this,
                    "Format email tidak valid",
                    Toast.LENGTH_SHORT
                ).show()

            }
            else if (
                databaseHelper.isEmailExists(emailText)
            ) {

                Toast.makeText(
                    this,
                    "Email sudah terdaftar",
                    Toast.LENGTH_SHORT
                ).show()

            }
            else if (
                passwordText != konfirmasiText
            ) {

                Toast.makeText(
                    this,
                    "Password tidak sama",
                    Toast.LENGTH_SHORT
                ).show()

            }
            else {

                val success = databaseHelper.insertUser(
                    namaText,
                    emailText,
                    passwordText
                )

                if (success) {

                    Toast.makeText(
                        this,
                        "Pendaftaran berhasil",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()

                } else {

                    Toast.makeText(
                        this,
                        "Pendaftaran gagal",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }

        txtMasuk.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}