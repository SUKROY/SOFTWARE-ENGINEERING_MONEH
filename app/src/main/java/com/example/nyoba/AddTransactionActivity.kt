package com.example.nyoba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.graphics.Color

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private var selectedType = "Income"

    private fun updateTypeButtonUI(
        btnIncome: Button,
        btnExpense: Button
    ) {

        if(selectedType == "Income") {

            btnIncome.setBackgroundColor(
                Color.parseColor("#4CAF50")
            )

            btnExpense.setBackgroundColor(
                Color.parseColor("#757575")
            )

        } else {

            btnIncome.setBackgroundColor(
                Color.parseColor("#757575")
            )

            btnExpense.setBackgroundColor(
                Color.parseColor("#F44336")
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_transaction)

        databaseHelper = DatabaseHelper(this)

        // =====================
        // INPUT
        // =====================

        val btnIncome =
            findViewById<Button>(R.id.btnIncome)

        val btnExpense =
            findViewById<Button>(R.id.btnExpense)

        updateTypeButtonUI(
            btnIncome,
            btnExpense
        )

        val inputAmount =
            findViewById<EditText>(R.id.inputAmount)

        val inputCategory =
            findViewById<AutoCompleteTextView>(
                R.id.inputCategory
            )
        val categoryList =
            databaseHelper.getAllCategories()

        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                categoryList
            )

        inputCategory.setAdapter(adapter)

        inputCategory.threshold = 1

        inputCategory.setOnClickListener {

            inputCategory.showDropDown()

        }


        inputCategory.setAdapter(adapter)

        val inputDate =
            findViewById<EditText>(R.id.inputDate)

        val calendar = Calendar.getInstance()

        inputDate.setOnClickListener {

            DatePickerDialog(
                this,
                { _, year, month, day ->

                    calendar.set(
                        year,
                        month,
                        day
                    )

                    val format =
                        SimpleDateFormat(
                            "dd MMM yyyy",
                            Locale.getDefault()
                        )

                    inputDate.setText(
                        format.format(calendar.time)
                    )

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)

            ).show()
        }

        val inputDescription =
            findViewById<EditText>(R.id.inputDescription)

        val btnSave =
            findViewById<Button>(R.id.btnSave)

        // =====================
        // BOTTOM NAVIGATION
        // =====================

        val btnSupport =
            findViewById<ImageButton>(R.id.btnSupport)

        val btnHome =
            findViewById<ImageButton>(R.id.btnHome)

        val btnAccount =
            findViewById<ImageButton>(R.id.btnAccount)

        // =====================
        // INCOME
        // =====================

        btnIncome.setOnClickListener {

            selectedType = "Income"

            updateTypeButtonUI(
                btnIncome,
                btnExpense
            )
        }

        // =====================
        // EXPENSE
        // =====================

        btnExpense.setOnClickListener {

            selectedType = "Expense"

            updateTypeButtonUI(
                btnIncome,
                btnExpense
            )
        } 

        // =====================
        // SAVE TRANSACTION
        // =====================

        btnSave.setOnClickListener {

            val amountText =
                inputAmount.text.toString().trim()

            val categoryText =
                inputCategory.text.toString().trim()

            val dateText =
                inputDate.text.toString().trim()

            val descriptionText =
                inputDescription.text.toString().trim()

            if (
                amountText.isEmpty() ||
                categoryText.isEmpty() ||
                dateText.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Lengkapi semua data",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val success =
                databaseHelper.insertTransaction(
                    selectedType,
                    amountText.toDouble(),
                    categoryText,
                    dateText,
                    descriptionText
                )

            if (success) {

                Toast.makeText(
                    this,
                    "Transaction Saved",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            } else {

                Toast.makeText(
                    this,
                    "Failed To Save Transaction",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

        // =====================
        // SUPPORT
        // =====================

        btnSupport.setOnClickListener {

            Toast.makeText(
                this,
                "Support Page Coming Soon",
                Toast.LENGTH_SHORT
            ).show()

        }

        // =====================
        // HOME
        // =====================

        btnHome.setOnClickListener {

            val intent =
                Intent(
                    this,
                    DashboardActivity::class.java
                )

            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP

            startActivity(intent)

            finish()

        }

        // =====================
        // ACCOUNT
        // =====================

        btnAccount.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AccountActivity::class.java
                )
            )
        }
    }
}