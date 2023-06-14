package com.nickfinance.nikfinance.domain.models

import android.os.Parcelable
import com.nickfinance.nikfinance.data.database.entity.ExpenseDBE
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense(
    val id: Long,
    val themeId: Long,
    val amount: Double,
    val createdAt: Long,
    val location: String?,
    val commentText: String?,
    val isArchived: Boolean = false
) : Parcelable {

    fun toExpenseDBE(): ExpenseDBE =
        ExpenseDBE(
            id = id,
            themeId = themeId,
            amount = amount,
            createdAt = createdAt,
            location = location,
            commentText = commentText,
            isArchived = isArchived
        )

}