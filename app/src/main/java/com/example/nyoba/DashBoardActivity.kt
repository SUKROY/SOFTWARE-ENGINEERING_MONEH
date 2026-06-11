package com.example.nyoba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class DashboardActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var txtIncomeValue: TextView
    private lateinit var txtExpenseValue: TextView
    private lateinit var txtBalanceValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        databaseHelper = DatabaseHelper(this)

        // ==========================
        // Summary
        // ==========================

        txtIncomeValue =
            findViewById(R.id.txtIncomeValue)

        txtExpenseValue =
            findViewById(R.id.txtExpenseValue)

        txtBalanceValue =
            findViewById(R.id.txtBalanceValue)

        // ==========================
        // Dashboard Menu
        // ==========================

        val btnAddTransaction =
            findViewById<Button>(
                R.id.btnAddTransaction
            )

        val btnTransactionHistory =
            findViewById<Button>(
                R.id.btnTransactionHistory
            )

        val btnFinancialInsight =
            findViewById<Button>(
                R.id.btnFinancialInsight
            )

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

        // ==========================
        // Load Data
        // ==========================

        loadFinancialSummary()

        loadExpenseChart()

        // ==========================
        // Add Transaction
        // ==========================

        btnAddTransaction.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AddTransactionActivity::class.java
                )
            )
        }

        // ==========================
        // Transaction History
        // ==========================

        btnTransactionHistory.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    TransactionHistoryActivity::class.java
                )
            )
        }

        // ==========================
        // Financial Insight
        // ==========================

        btnFinancialInsight.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    FinancialInsightActivity::class.java
                )
            )
        }

        val btnBudgetPlanner =
            findViewById<Button>(
                R.id.btnBudgetPlanner
            )

        btnBudgetPlanner.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    BudgetPlannerActivity::class.java
                )
            )
        }

        // ==========================
        // Support
        // ==========================

        btnSupport.setOnClickListener {

            Toast.makeText(
                this,
                "Support Page Coming Soon",
                Toast.LENGTH_SHORT
            ).show()
        }

        // ==========================
        // Home
        // ==========================

        btnHome.setOnClickListener {

            Toast.makeText(
                this,
                "Anda sudah berada di Home",
                Toast.LENGTH_SHORT
            ).show()
        }

        // ==========================
        // Account
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

        loadFinancialSummary()

        loadExpenseChart()
    }

    // ==========================
    // Financial Summary
    // ==========================

    private fun loadFinancialSummary() {

        val income =
            databaseHelper.getTotalIncome()

        val expense =
            databaseHelper.getTotalExpense()

        val balance =
            databaseHelper.getBalance()

        txtIncomeValue.text =
            "Rp %.0f".format(income)

        txtExpenseValue.text =
            "Rp %.0f".format(expense)

        txtBalanceValue.text =
            "Rp %.0f".format(balance)
    }

    // ==========================
    // Monthly Balance Chart
    // ==========================

    private fun loadExpenseChart() {

        val chart =
            findViewById<LineChart>(
                R.id.lineChartExpense
            )

        val monthlyData =
            databaseHelper.getMonthlyBalance()

        val entries =
            ArrayList<Entry>()

        val labels =
            ArrayList<String>()

        for(i in monthlyData.indices) {

            entries.add(
                Entry(
                    i.toFloat(),
                    monthlyData[i].balance.toFloat()
                )
            )

            labels.add(
                monthlyData[i].month
            )
        }

        val dataSet =
            LineDataSet(
                entries,
                "Monthly Balance"
            )

        dataSet.lineWidth = 3f

        dataSet.circleRadius = 5f

        dataSet.setDrawValues(true)

        dataSet.mode =
            LineDataSet.Mode.HORIZONTAL_BEZIER

        dataSet.setDrawFilled(true)

        val lineData =
            LineData(dataSet)

        chart.data =
            lineData

        chart.description.isEnabled =
            false

        chart.legend.isEnabled =
            true

        chart.axisRight.isEnabled =
            false

        chart.setScaleEnabled(false)

        chart.setPinchZoom(false)

        chart.isDragEnabled =
            false

        chart.xAxis.position =
            XAxis.XAxisPosition.BOTTOM

        chart.xAxis.granularity =
            1f

        chart.xAxis.labelCount =
            labels.size

        chart.xAxis.labelRotationAngle =
            -30f

        chart.xAxis.valueFormatter =
            IndexAxisValueFormatter(
                labels
            )

        chart.xAxis.setDrawGridLines(false)

        chart.animateX(1000)

        chart.invalidate()
    }
}