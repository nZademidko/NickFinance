package com.nickfinance.nikfinance.domain.models

import com.nickfinance.nikfinance.data.database.entity.ExpenseDBE
import java.util.*

data class AddExpenseData(
    var amount: String? = null,
    var textComment: String? = null,
    var theme: Theme? = null,
    var themeId: Long = -1,
    var createdAt: Date = Date(System.currentTimeMillis()),
    var location: String? = null
) {
    fun toExpenseDBE() = ExpenseDBE(
        themeId = theme!!.id,
        amount = amount!!.toDouble(),
        createdAt = createdAt.time,
        location = location,
        commentText = textComment
    )
}