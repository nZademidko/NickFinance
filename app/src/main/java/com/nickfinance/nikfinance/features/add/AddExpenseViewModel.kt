package com.nickfinance.nikfinance.features.add

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.nickfinance.nikfinance.base.BaseRowHolder
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.data.models.Theme
import com.nickfinance.nikfinance.domain.models.AddExpenseData
import com.nickfinance.nikfinance.domain.repositories.abstractions.MainRepository
import com.nickfinance.nikfinance.features.add.adapter.ThemesRowHolder
import com.nickfinance.nikfinance.base.executor.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
) : BaseViewModel(application) {

    private val _themesUiState = MutableStateFlow<ThemesUiState>(ThemesUiState.Loading)
    val themesUiState = _themesUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = ThemesUiState.Loading
    )
    val buttonState = MutableStateFlow(false)
    val navigateState = MutableSharedFlow<NavDirections>()

    private var addExpenseData = MutableStateFlow(AddExpenseData())

    init {
        viewModelScope.launch {
            addExpenseData.collect {
                checkFills()
            }
        }
        getThemes()
    }

    private fun getThemes(selectedTheme: Theme? = null) {
        doFlowJob(
            onProgress = {
                mainRepository.getThemes()
            },
            onFinish = { result ->
                when (result) {
                    is Result.Success -> {
                        _themesUiState.value = ThemesUiState.Success(
                            data = result.data.map {
                                ThemesRowHolder(
                                    theme = it,
                                    isSelected = selectedTheme == it,
                                    toSelect = {
                                        addExpenseData.value = addExpenseData.value.copy(theme = it)
                                        getThemes(it)
                                    }
                                )
                            }
                        )
                    }

                    is Result.Error -> {}
                }
            }
        )
    }

    fun onAmountTextChanged(text: String) {
        addExpenseData.value = addExpenseData.value.copy(amount = text)
    }

    fun onCommentTextChanged(text: String) {
        addExpenseData.value = addExpenseData.value.copy(textComment = text)
    }

    fun save() {
        viewModelScope.launch {
            mainRepository.saveExpense(data = addExpenseData.value)
            navigateState.emit(AddExpenseFragmentDirections.toMainFragment())
        }
    }

    private fun checkFills() {
        with(addExpenseData.value) {
            buttonState.value = !amount.isNullOrBlank() && theme != null
        }
    }


    sealed class ThemesUiState {

        object Loading : ThemesUiState()

        class Success(val data: List<BaseRowHolder>) : ThemesUiState()

    }
}