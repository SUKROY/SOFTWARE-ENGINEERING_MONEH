package com.example.nyoba

data class HistoryItem(
    val isHeader: Boolean,
    val headerDate: String = "",
    val transaction: Transaction? = null
)