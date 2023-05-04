package com.nickfinance.nikfinance.features.main

import androidx.annotation.StringRes
import com.nickfinance.nikfinance.R

enum class MainTab(val key: Int, @StringRes val title: Int) {
    LIST(title = R.string.main_tab_list, key = 0),
    CHART(title = R.string.main_tab_chart, key = 1)
}