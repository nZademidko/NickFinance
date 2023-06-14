package com.nickfinance.nikfinance.features.main.pager

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.base.PermissionFragment
import com.nickfinance.nikfinance.base.executor.Result
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.domain.repositories.abstractions.MainRepository
import com.nickfinance.nikfinance.domain.models.FilterData
import com.nickfinance.nikfinance.utils.extensions.getString
import com.nickfinance.nikfinance.utils.extensions.parcelable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
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
    private val chooseFilterChannel = Channel<FilterData>()
    val chooseFilterAction = chooseFilterChannel.receiveAsFlow()

    private lateinit var data: List<ExpenseWithTheme>
    private var filters = FilterData()

    init {
        checkPushNotificationAccess()
        getExpenses()
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            getString(R.string.key_filter) -> {
                filters = result.parcelable(getString(R.string.data_filter))!!
                getExpenses()
            }
        }
    }

    fun onFilterClicked() {
        viewModelScope.launch {
            chooseFilterChannel.send(filters)
        }
    }

    fun toExpensesMap() {
        navigateTo(MainPagerFragmentDirections.toMap(data = data.toTypedArray()))
    }

    fun toAddExpense() {
        navigateTo(MainPagerFragmentDirections.toAddExpense())
    }

    fun toAddTheme() {
        navigateTo(MainPagerFragmentDirections.toAddTheme())
    }

    private fun checkPushNotificationAccess() {
        requestPermission(
            reqPermissions = object : PermissionFragment.RequestPermissions() {},
            list = listOf(PermissionFragment.PermissionType.RECEIVE_BOOT_COMPLETED)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermission(
                reqPermissions = object : PermissionFragment.RequestPermissions() {},
                list = listOf(PermissionFragment.PermissionType.POST_NOTIFICATION)
            )
        }
    }

    private fun getExpenses() {
        doFlowJob(
            onProgress = {
                mainRepository.getExpensesWithThemes(filters)
            },
            onFinish = { result ->
                when (result) {
                    is Result.Success -> {
                        data = result.data
                        _mainPagerUiState.value = MainPagerUiState.Success(result.data)
                    }

                    is Result.Error -> {}
                }
            }
        )
    }

    sealed class MainPagerUiState {

        object Loading : MainPagerUiState()

        class Success(val data: List<ExpenseWithTheme>) : MainPagerUiState()

    }
}