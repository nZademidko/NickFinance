package com.nickfinance.nikfinance.utils.extensions

import android.app.Application
import androidx.annotation.StringRes
import com.nickfinance.nikfinance.base.BaseViewModel

fun BaseViewModel.getString(@StringRes stringRes: Int): String {
    return this.getApplication<Application>().resources.getString(stringRes)
}