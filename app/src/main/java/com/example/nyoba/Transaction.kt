package com.example.nyoba

data class Transaction(
    val id: Int,
    val type: String,
    val amount: Double,
    val category: String,
    val date: String,
    val description: String
)