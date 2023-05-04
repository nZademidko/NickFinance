package com.nickfinance.nikfinance.features.main.pager

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.base.executor.Result
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.domain.repositories.abstractions.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPagerViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
) : BaseViewModel(application) {

    private val _mainPagerUiState = MutableStateFlow<MainPagerUiState>(MainPagerUiState.Loading)
    val mainPagerUiState: StateFlow<MainPagerUiState> = _mainPagerUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = MainPagerUiState.Loading
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
                        _mainPagerUiState.value = MainPagerUiState.Success(result.data)
                    }

                    is Result.Error -> {}
                }
            }
        )
    }



    fun toAddExpense() = viewModelScope.launch {
        navigationAction.send(NavigationAction.navigateTo(MainPagerFragmentDirections.toAddExpense()))
    }

    sealed class MainPagerUiState {

        object Loading : MainPagerUiState()

        class Success(val data: List<ExpenseWithTheme>) : MainPagerUiState()

    }
}