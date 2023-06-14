package com.nickfinance.nikfinance.utils

import java.text.SimpleDateFormat
import java.util.*

class DateFormats {

    companion object {
        enum class Format(val value: String) {
            DD_MMMM(value = "dd MMMM"),
            HH_MM(value = "HH:mm"),
            DD_MMMM_YYYY(value = "dd MMMM yyyy"),
            DD_MM_YYYY(value = "dd.MM.yyyy"),
            DD_MM_YY(value = "dd.MM.yy"),
            DD_MM(value = "dd.MM"),
            DD_MM_YYYY_HH_MM(value = "dd.MM.yyyy HH:mm"),
            YYYY(value = "yyyy")
        }

        fun getDate(value: String, format: Format): Date {
            val formatDate = SimpleDateFormat(format.value, Locale.getDefault())
            formatDate.timeZone = TimeZone.getTimeZone("gmt")
            return formatDate.parse(value)!!
        }

        fun getDateOrNull(value: String?, format: Format): Date? {
            val formatDate = SimpleDateFormat(format.value, Locale.getDefault())
            formatDate.timeZone = TimeZone.getTimeZone("gmt")
            return formatDate.parseOrNull(value)
        }

        fun getString(value: Date, format: Format): String {
            return SimpleDateFormat(format.value, Locale.getDefault()).format(value)
        }

        fun getStringOrNull(value: Date?, format: Format): String? {
            return if (value == null) {
                null
            } else {
                SimpleDateFormat(format.value, Locale.getDefault()).format(value)
            }
        }

    }
}

fun SimpleDateFormat.parseOrNull(string: String?) =
    try {
        isLenient = false
        string?.let { parse(string) }
    } catch (e: Exception) {
        null
    }
