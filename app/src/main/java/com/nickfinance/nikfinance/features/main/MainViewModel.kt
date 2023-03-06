package com.nickfinance.nikfinance.features.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.nickfinance.nikfinance.base.BaseRowHolder
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.base.executor.Result
import com.nickfinance.nikfinance.data.models.Expense
import com.nickfinance.nikfinance.data.models.Theme
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.domain.repositories.abstractions.MainRepository
import com.nickfinance.nikfinance.features.add.AddExpenseViewModel
import com.nickfinance.nikfinance.features.add.adapter.ThemesRowHolder
import com.nickfinance.nikfinance.features.main.adapter.MainListExpensesRowHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
) : BaseViewModel(application) {

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
        doFlowJob(
            onProgress = {
                mainRepository.getExpensesWithThemes()
            },
            onFinish = { result ->
                when (result) {
                    is Result.Success -> {
                        _mainUiState.value = MainUiState.Success(
                            result.data.map {
                                MainListExpensesRowHolder(expenseWithTheme = it)
                            }
                        )
                    }

                    is Result.Error -> {}
                }
            }
        )
    }

    sealed class MainUiState {

        object Loading : MainUiState()

        class Success(val data: List<BaseRowHolder>) : MainUiState()

    }

}