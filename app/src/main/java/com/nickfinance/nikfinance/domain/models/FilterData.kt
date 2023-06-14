package com.nickfinance.nikfinance.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterData(
    val minAmount: Double? = null,
    val maxAmount: Double? = null,
    val minCreatedAt: Long? = null,
    val maxCreatedAt: Long? = null,
    val isArchived: Boolean = false
) : Parcelable