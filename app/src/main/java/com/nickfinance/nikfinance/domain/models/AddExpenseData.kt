package com.nickfinance.nikfinance.domain.models

import com.nickfinance.nikfinance.data.database.entity.ExpenseDBE
import com.nickfinance.nikfinance.data.models.Theme

data class AddExpenseData(
    var amount: String? = null,
    var textComment: String? = null,
    var theme: Theme? = null,
    var themeId: Long = -1,
    var location: String? = null
) {
    fun toExpenseDBE() = ExpenseDBE(
        themeId = theme!!.id,
        amount = amount!!.toDouble(),
        createdAt = System.currentTimeMillis(),
        location = null,
        commentText = textComment
    )
}