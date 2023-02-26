package com.nickfinance.nikfinance.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.nickfinance.nikfinance.data.models.Expense

@Entity(
    tableName = "expenses"
)
data class ExpenseDBE(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "theme_id") val themeId: Long,
    val amount: Double,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    val location: String?,
    @ColumnInfo(name = "comment_text") val commentText: String?
) {

    fun toExpense(): Expense = Expense(
        id = id,
        themeId = themeId,
        amount = amount,
        createdAt = createdAt,
        location = location,
        commentText = commentText
    )

}