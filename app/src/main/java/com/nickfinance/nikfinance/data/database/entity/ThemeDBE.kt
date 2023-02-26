package com.nickfinance.nikfinance.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nickfinance.nikfinance.data.models.Theme

@Entity(tableName = "themes")
data class ThemeDBE(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val color: String
) {

    fun toTheme(): Theme = Theme(
        id = id,
        name = name,
        color = color
    )

}