package com.example.nyoba

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TransactionHistoryActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_transaction_history
        )

        databaseHelper = DatabaseHelper(this)

        val recycler =
            findViewById<RecyclerView>(
                R.id.recyclerTransaction
            )

        recycler.layoutManager =
            LinearLayoutManager(this)

        updateSummary()

        refreshTransactionList()

        // ==========================
        // Bottom Navigation
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

        btnSupport.setOnClickListener {

            Toast.makeText(
                this,
                "Support Page Coming Soon",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnHome.setOnClickListener {

            val intent =
                Intent(
                    this,
                    DashboardActivity::class.java
                )

            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP

            startActivity(intent)
        }

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

        updateSummary()

        refreshTransactionList()
    }

    // ==========================
    // Refresh Transaction List
    // ==========================

    private fun refreshTransactionList() {

        val recycler =
            findViewById<RecyclerView>(
                R.id.recyclerTransaction
            )

        recycler.adapter =
            TransactionAdapter(
                databaseHelper.getTransactionList()
            ) { transaction ->

                val success =
                    databaseHelper.deleteTransaction(
                        transaction.id
                    )

                if (success) {

                    Toast.makeText(
                        this,
                        "Transaction Deleted",
                        Toast.LENGTH_SHORT
                    ).show()

                    updateSummary()

                    refreshTransactionList()

                } else {

                    Toast.makeText(
                        this,
                        "Failed To Delete Transaction",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    // ==========================
    // Update Summary
    // ==========================

    private fun updateSummary() {

        val txtIncome =
            findViewById<TextView>(
                R.id.txtIncome
            )

        val txtExpense =
            findViewById<TextView>(
                R.id.txtExpense
            )

        val txtBalance =
            findViewById<TextView>(
                R.id.txtBalance
            )

        txtIncome.text =
            "Rp %.0f".format(
                databaseHelper.getTotalIncome()
            )

        txtExpense.text =
            "Rp %.0f".format(
                databaseHelper.getTotalExpense()
            )

        txtBalance.text =
            "Rp %.0f".format(
                databaseHelper.getBalance()
            )
    }
}