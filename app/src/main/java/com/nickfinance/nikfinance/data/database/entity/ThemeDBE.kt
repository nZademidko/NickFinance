package com.nickfinance.nikfinance.data.database.entity

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nickfinance.nikfinance.base.ThemeIconState
import com.nickfinance.nikfinance.domain.models.Theme

@Entity(tableName = "themes")
data class ThemeDBE(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val color: String,
    @ColumnInfo(name = "icon_id") val iconId: String
) {

    fun toTheme(): Theme = Theme(
        id = id,
        iconId = ThemeIconState.values().find { it.value == iconId }!!.resId,
        name = name,
        color = Color.parseColor(color),
    )

}