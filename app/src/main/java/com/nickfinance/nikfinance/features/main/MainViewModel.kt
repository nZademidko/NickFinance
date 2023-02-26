package com.nickfinance.nikfinance.features.main

import androidx.lifecycle.viewModelScope
import com.nickfinance.nikfinance.base.BaseRowHolder
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.data.models.Expense
import com.nickfinance.nikfinance.data.models.Theme
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.domain.repositories.abstractions.MainRepository
import com.nickfinance.nikfinance.features.main.adapter.MainListExpensesRowHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    private val _mainUiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val mainUiState: StateFlow<MainUiState> = _mainUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = MainUiState.Loading
    )

    init {
        getExpenses()
    }

    private fun getExpenses() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mainRepository.getExpensesWithThemes().collect { list ->
                    _mainUiState.value = MainUiState.Success(
                        data = list.map { MainListExpensesRowHolder(expenseWithTheme = it) }
                    )
                }
            }
        }
    }

    sealed class MainUiState {

        object Loading : MainUiState()

        class Success(val data: List<BaseRowHolder>) : MainUiState()

    }

}