package com.example.nyoba

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class FinancialInsightActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_financial_insight
        )

        databaseHelper = DatabaseHelper(this)

        val txtIncome =
            findViewById<TextView>(R.id.txtIncome)

        val txtExpense =
            findViewById<TextView>(R.id.txtExpense)

        val txtBalance =
            findViewById<TextView>(R.id.txtBalance)

        val layoutStatistic =
            findViewById<LinearLayout>(
                R.id.layoutStatistic
            )

        val layoutDistribution =
            findViewById<LinearLayout>(
                R.id.layoutDistribution
            )

        val txtAdvice =
            findViewById<TextView>(
                R.id.txtAdvice
            )

        val categoryData =
            databaseHelper.getExpenseByCategory()

        val pieChart =
            findViewById<PieChart>(
                R.id.pieChart
            )

        val entries =
            ArrayList<PieEntry>()

        for ((category, amount) in categoryData) {

            entries.add(
                PieEntry(
                    amount.toFloat(),
                    category
                )
            )
        }

        val dataSet =
            PieDataSet(
                entries,
                "Expense Distribution"
            )

        dataSet.colors =
            ColorTemplate.MATERIAL_COLORS.toList()

        dataSet.valueTextSize =
            14f

        val pieData =
            PieData(dataSet)

        pieChart.data =
            pieData

        pieChart.description.isEnabled =
            false

        pieChart.legend.isEnabled =
            true

        pieChart.centerText =
            "Expenses"

        pieChart.setCenterTextSize(
            18f
        )

        pieChart.isDrawHoleEnabled =
            true

        pieChart.holeRadius =
            60f

        pieChart.transparentCircleRadius =
            65f

        pieChart.animateY(
            1200
        )

        pieChart.invalidate()

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

        var totalExpense = 0.0

        for(value in categoryData.values) {
            totalExpense += value
        }

        var biggestCategory = ""
        var biggestAmount = 0.0

        for((category, amount) in categoryData) {

            if(amount > biggestAmount) {
                biggestAmount = amount
                biggestCategory = category
            }

            val statisticText =
                TextView(this)

            statisticText.text =
                "$category : Rp %.0f".format(amount)

            layoutStatistic.addView(
                statisticText
            )

            val percent =
                if(totalExpense > 0)
                    (amount / totalExpense * 100)
                else
                    0.0

            val distributionText =
                TextView(this)

            distributionText.text =
                "$category : %.1f%%".format(percent)

            layoutDistribution.addView(
                distributionText
            )
        }

        txtAdvice.text =
            if(biggestCategory.isNotEmpty()) {

                """
Kategori pengeluaran terbesar:

$biggestCategory

Rp %.0f

Cobalah mengurangi pengeluaran pada kategori ini untuk meningkatkan saldo.
                """.trimIndent().format(biggestAmount)

            } else {

                "Belum ada data transaksi."
            }

        // =====================
        // Bottom Navigation
        // =====================

        findViewById<ImageButton>(
            R.id.btnHome
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    DashboardActivity::class.java
                )
            )
        }

        findViewById<ImageButton>(
            R.id.btnSupport
        ).setOnClickListener {

            Toast.makeText(
                this,
                "Support Coming Soon",
                Toast.LENGTH_SHORT
            ).show()
        }

        findViewById<ImageButton>(
            R.id.btnAccount
        ).setOnClickListener {

            Toast.makeText(
                this,
                "Account Coming Soon",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}