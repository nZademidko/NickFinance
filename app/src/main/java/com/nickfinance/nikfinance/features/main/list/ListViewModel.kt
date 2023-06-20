package com.nickfinance.nikfinance.features.main.list

import android.app.Application
import com.nickfinance.nikfinance.base.adapter.BaseRowHolder
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.base.adapter.holders.SingleLineViewHolder
import com.nickfinance.nikfinance.base.adapter.holders.TimeHolder
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.views.builders.*
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.features.main.pager.MainPagerFragmentDirections
import com.nickfinance.nikfinance.utils.DateFormats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    application: Application
) : BaseViewModel(application) {

    private val _items = MutableStateFlow<ListUiState>(ListUiState.Loading)
    val items = _items.asStateFlow()

    fun initList(data: List<ExpenseWithTheme>) {
        var curTime: Long? = null
        val list = mutableListOf<BaseRowHolder>()
        data.forEach { item ->
            val createdAtCalendar = Calendar.getInstance()
            createdAtCalendar.timeInMillis = item.expense.createdAt
            createdAtCalendar.set(Calendar.HOUR_OF_DAY, 0)
            createdAtCalendar.set(Calendar.MINUTE, 0)
            createdAtCalendar.set(Calendar.SECOND, 0)
            createdAtCalendar.set(Calendar.MILLISECOND, 0)
            if (curTime == null || curTime != createdAtCalendar.timeInMillis) {
                curTime = createdAtCalendar.timeInMillis
                val todayCalendar = Calendar.getInstance()
                todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
                todayCalendar.set(Calendar.MINUTE, 0)
                todayCalendar.set(Calendar.SECOND, 0)
                todayCalendar.set(Calendar.MILLISECOND, 0)
                val result = if (todayCalendar.timeInMillis == curTime!!) {
                    "Сегодня"
                } else if (todayCalendar.timeInMillis - curTime!! <= 86_400_000) {
                    "Вчера"
                } else {
                    DateFormats.getString(
                        Date(item.expense.createdAt),
                        DateFormats.Companion.Format.DD_MMMM
                    )
                }
                list.add(TimeHolder(time = result))
            }
            list.add(SingleLineViewHolder(
                builder = ViewSingleLineBuilder.newBuilder()
                    .setStateIconLeft(
                        IconState.ShowFromRes(
                            iconId = item.theme.iconId,
                            colorState = ColorState.FromIntColor(item.theme.color)
                        )
                    )
                    .setStateTextLeft(
                        TextState.Show(
                            value = item.theme.name,
                            colorState = ColorState.FromIntColor(item.theme.color)
                        )
                    )
                    .setStateTextRight(
                        TextState.Show(
                            value = item.expense.amount.toString()
                        )
                    )
                    .setStateRoot(
                        RootState.Fill(
                            clickState = ClickState.Action {
                                toFullExpenseInfo(item)
                            }
                        )
                    )
                    .build()
            ))
        }
//        data.forEach { item ->
//            if (curTime == null || curTime != item.expense.createdAt - (item.expense.createdAt % 86_400_000)) {
//                curTime = item.expense.createdAt - (item.expense.createdAt % 86_400_000)
//                val timeInMIlls = Calendar.getInstance().timeInMillis
//                val result = if (timeInMIlls - (timeInMIlls % 86_400_000) == curTime!!) {
//                    "Сегодня"
//                } else if (timeInMIlls - (timeInMIlls % 86_400_000) - curTime!! <= 86_400_000) {
//                    "Вчера"
//                } else {
//                    DateFormats.getString(
//                        Date(item.expense.createdAt),
//                        DateFormats.Companion.Format.DD_MMMM
//                    )
//                }
//                list.add(TimeHolder(time = result))
//            }
//            list.add(SingleLineViewHolder(
//                builder = ViewSingleLineBuilder.newBuilder()
//                    .setStateIconLeft(
//                        IconState.ShowFromRes(
//                            iconId = item.theme.iconId,
//                            colorState = ColorState.FromIntColor(item.theme.color)
//                        )
//                    )
//                    .setStateTextLeft(
//                        TextState.Show(
//                            value = item.theme.name,
//                            colorState = ColorState.FromIntColor(item.theme.color)
//                        )
//                    )
//                    .setStateTextRight(
//                        TextState.Show(
//                            value = item.expense.amount.toString()
//                        )
//                    )
//                    .setStateRoot(
//                        RootState.Fill(
//                            clickState = ClickState.Action {
//                                toFullExpenseInfo(item)
//                            }
//                        )
//                    )
//                    .build()
//            ))
//        }
        _items.value = ListUiState.Success(list)
    }

    private fun toFullExpenseInfo(item: ExpenseWithTheme) {
        navigateTo(MainPagerFragmentDirections.toDetailedExpense(item))
    }

    sealed class ListUiState {

        object Loading : ListUiState()

        class Success(val data: List<BaseRowHolder>) : ListUiState()

    }
}