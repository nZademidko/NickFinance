package com.nickfinance.nikfinance.data.models

data class Expense(
    val id: Long,
    val themeId: Long,
    val amount: Double,
    val createdAt: Long,
    val location: String?,
    val commentText: String?
)