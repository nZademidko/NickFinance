package com.nickfinance.nikfinance.features.main.list

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.nickfinance.nikfinance.base.adapter.BaseRowHolder
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.base.adapter.holders.SingleLineViewHolder
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.views.builders.*
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.domain.repositories.abstractions.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
) : BaseViewModel(application) {

    private val _items = MutableStateFlow<ListUiState>(ListUiState.Loading)
    val items = _items.asStateFlow()

    fun initList(data: List<ExpenseWithTheme>) {
        _items.value = ListUiState.Success(
            data.map { item ->
                SingleLineViewHolder(
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
                )
            }
        )
    }

    private fun toFullExpenseInfo(item: ExpenseWithTheme) = viewModelScope.launch {
        //      navigationAction.send()
    }

    sealed class ListUiState {

        object Loading : ListUiState()

        class Success(val data: List<BaseRowHolder>) : ListUiState()

    }
}