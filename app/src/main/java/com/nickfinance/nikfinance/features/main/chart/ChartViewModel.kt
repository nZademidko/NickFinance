package com.nickfinance.nikfinance.features.main.chart

import android.app.Application
import android.graphics.Color
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.ArrayMap
import androidx.collection.arrayMapOf
import android.text.*
import androidx.lifecycle.viewModelScope
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.base.adapter.BaseRowHolder
import com.nickfinance.nikfinance.base.adapter.holders.ThemeHolder
import com.nickfinance.nikfinance.domain.models.Theme
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.features.main.pager.MainPagerViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.views.builders.*
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    application: Application
) : BaseViewModel(application) {

    private val _chartStateBuilder = MutableStateFlow<ChartUiState>(ChartUiState.Loading)
    val chartStateBuilder = _chartStateBuilder.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = MainPagerViewModel.MainPagerUiState.Loading
    )

    private val _themes = MutableStateFlow<ThemesUiState>(ThemesUiState.Loading)
    val themes = _themes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = MainPagerViewModel.MainPagerUiState.Loading
    )

    fun initList(data: List<ExpenseWithTheme>) {
        val map = ArrayMap<String, Pair<Double, Int>>()
        data.map { item ->
            val curName = item.theme.name
            val curAmount: Double = (map[curName]?.first ?: 0.0) + item.expense.amount
            map[curName] = Pair(curAmount, item.theme.color)
        }
        val generalSum = data.sumOf { it.expense.amount }
        val builder = ViewChartBuilder.newBuilder()
            .setData(map)
            .setTextCenterState(
                TextState.Show(
                    value = "Общие траты",
                    spannable = SpannableString(generalSum.toString()).apply {
                        setSpan(
                            ForegroundColorSpan(Color.BLACK),
                            0,
                            generalSum.toString().length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                )
            )
            .setClickAction { theme ->

            }
            .build()
        _chartStateBuilder.value = ChartUiState.Success(builder = builder)

        initThemes(data)
    }

    private fun initThemes(data: List<ExpenseWithTheme>) {
        val map = arrayMapOf<Theme, Double>()
        data.forEach { item -> map[item.theme] = (map[item.theme] ?: 0.0) + item.expense.amount }
        val holders = map.map { entry ->
            getThemeHolder(entry.key, entry.value)
        }
        _themes.value = ThemesUiState.Success(
            firstThemes = holders.subList(0, holders.size / 2),
            secondThemes = holders.subList(holders.size / 2, holders.size)
        )
    }

    private fun getThemeHolder(theme: Theme, amount: Double): ThemeHolder {
        return ThemeHolder(
            builder = ViewThemeBuilder.newBuilder()
                .setThemeState(
                    TextState.Show(
                        colorState = ColorState.FromIntColor(theme.color),
                        styleId = R.style.TextAppearance_App_Body_B1_text_accent_16,
                        value = theme.name
                    )
                )
                .setAmountState(
                    TextState.Show(
                        value = amount.toString()
                    )
                )
                .setIconState(
                    IconState.ShowFromRes(
                        iconId = theme.iconId,
                        colorState = ColorState.FromResource(R.color.white)
                    )
                )
                .setStateRoot(
                    RootState.Fill(
                        colorState = ColorState.FromIntColor(theme.color),
                        clickState = ClickState.Action {}
                    )
                )
                .build()
        )
    }

    sealed class ChartUiState {

        object Loading : ChartUiState()

        class Success(val builder: ViewChartBuilder) : ChartUiState()

    }

    sealed class ThemesUiState {

        object Loading : ThemesUiState()

        class Success(
            val firstThemes: List<BaseRowHolder>,
            val secondThemes: List<BaseRowHolder>
        ) : ThemesUiState()
    }
}