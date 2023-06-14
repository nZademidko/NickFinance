package com.nickfinance.nikfinance.features.detailedExpense

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.domain.repositories.abstractions.MainRepository
import com.nickfinance.nikfinance.base.executor.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class DetailedExpenseViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository
) : BaseViewModel(application) {

    private var data = DetailedExpenseFragmentArgs.fromSavedStateHandle(savedStateHandle).data
    private val _detailedExpenseUiState = MutableStateFlow<DetailedExpenseUiState>(
        DetailedExpenseUiState.Success(data)
    )
    val detailedExpenseUiState: StateFlow<DetailedExpenseUiState> =
        _detailedExpenseUiState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = DetailedExpenseUiState.Loading
        )

    val btnArchiveStateAction = MutableStateFlow("Архивировать")

    init {
        initUi()
    }

    private fun initUi() {
        btnArchiveStateAction.value =
            if (data.expense.isArchived) "Убрать из архива"
            else "Архивировать"
    }

    fun onMapMenuClicked() {
        if (data.expense.location.isNullOrBlank()) {
            showSnackBar("Локация данной траты не найдена")
        } else {
            navigateTo(DetailedExpenseFragmentDirections.toMapFragment(arrayOf(data)))
        }
    }

    fun archiveExpense() {
        doJob(
            onProgress = {
                data = data.copy(expense = data.expense.copy(isArchived = !data.expense.isArchived))
                mainRepository.updateExpense(data.expense)
            },

            onFinish = { result ->
                when (result) {
                    is Result.Success -> {
                        showSnackBar(
                            if (data.expense.isArchived) "Трата убрана в архив"
                            else "Трата убрана из архива"
                        )
                        initUi()
                    }

                    is Result.Error -> {}
                }
            }
        )
    }

    fun deleteExpense() {
        doJob(
            onProgress = {
                mainRepository.deleteExpense(data.expense)
            },

            onFinish = { result ->
                when (result) {
                    is Result.Success -> {
                        showSnackBar("Трата удалена")
                        navigateTo(DetailedExpenseFragmentDirections.toMainPagerFragment())
                    }

                    is Result.Error -> {}
                }
            }
        )
    }

    sealed class DetailedExpenseUiState {

        object Loading : DetailedExpenseUiState()

        class Success(val data: ExpenseWithTheme) : DetailedExpenseUiState()

    }
}