package com.example.nyoba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BudgetPlannerActivity :
    AppCompatActivity() {

    private lateinit var databaseHelper:
            DatabaseHelper

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_budget_planner
        )

        databaseHelper =
            DatabaseHelper(this)

        // ==========================
        // COMPONENT
        // ==========================

        val inputBudget =
            findViewById<EditText>(
                R.id.inputBudget
            )

        val btnSaveBudget =
            findViewById<Button>(
                R.id.btnSaveBudget
            )

        val txtBudget =
            findViewById<TextView>(
                R.id.txtBudget
            )

        val txtUsed =
            findViewById<TextView>(
                R.id.txtUsed
            )

        val txtStatus =
            findViewById<TextView>(
                R.id.txtStatus
            )

        val progressBar =
            findViewById<ProgressBar>(
                R.id.progressBudget
            )

        // ==========================
        // BOTTOM NAVIGATION
        // ==========================

        val btnSupport =
            findViewById<ImageButton>(
                R.id.btnSupport
            )

        val btnHome =
            findViewById<ImageButton>(
                R.id.btnHome
            )

        val btnAccount =
            findViewById<ImageButton>(
                R.id.btnAccount
            )

        // ==========================
        // LOAD DATA
        // ==========================

        loadBudget(
            txtBudget,
            txtUsed,
            txtStatus,
            progressBar
        )

        // ==========================
        // SAVE BUDGET
        // ==========================

        btnSaveBudget.setOnClickListener {

            val budgetText =
                inputBudget.text.toString()
                    .trim()

            if(budgetText.isEmpty()) {

                Toast.makeText(
                    this,
                    "Masukkan Budget",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val success =
                databaseHelper.saveBudget(
                    budgetText.toDouble()
                )

            if(success) {

                Toast.makeText(
                    this,
                    "Budget Saved",
                    Toast.LENGTH_SHORT
                ).show()

                inputBudget.setText("")

                loadBudget(
                    txtBudget,
                    txtUsed,
                    txtStatus,
                    progressBar
                )

            } else {

                Toast.makeText(
                    this,
                    "Failed To Save Budget",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // ==========================
        // SUPPORT
        // ==========================

        btnSupport.setOnClickListener {

            Toast.makeText(
                this,
                "Support Page Coming Soon",
                Toast.LENGTH_SHORT
            ).show()
        }

        // ==========================
        // HOME
        // ==========================

        btnHome.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    DashboardActivity::class.java
                )
            )

            finish()
        }

        // ==========================
        // ACCOUNT
        // ==========================

        btnAccount.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AccountActivity::class.java
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()

        loadBudget(
            findViewById(R.id.txtBudget),
            findViewById(R.id.txtUsed),
            findViewById(R.id.txtStatus),
            findViewById(R.id.progressBudget)
        )
    }

    // ==========================
    // LOAD BUDGET
    // ==========================

    private fun loadBudget(
        txtBudget: TextView,
        txtUsed: TextView,
        txtStatus: TextView,
        progressBar: ProgressBar
    ) {

        val budget =
            databaseHelper.getBudget()

        val expense =
            databaseHelper.getTotalExpense()

        txtBudget.text =
            "Budget : Rp %.0f"
                .format(budget)

        txtUsed.text =
            "Used : Rp %.0f"
                .format(expense)

        if(budget <= 0) {

            progressBar.progress = 0

            txtStatus.text =
                "Belum ada budget"

            return
        }

        val percent =
            ((expense / budget) * 100)
                .toInt()

        progressBar.progress =
            percent.coerceAtMost(100)

        when {

            percent >= 100 -> {

                txtStatus.text =
                    "🚨 Budget Habis"
            }

            percent >= 90 -> {

                txtStatus.text =
                    "⚠ Budget Hampir Habis"
            }

            else -> {

                txtStatus.text =
                    "✅ Budget Aman"
            }
        }
    }
}