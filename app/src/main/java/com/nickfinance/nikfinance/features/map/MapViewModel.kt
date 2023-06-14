package com.nickfinance.nikfinance.features.map

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {

    private val data = MapFragmentArgs.fromSavedStateHandle(savedStateHandle).data
    val state = MutableStateFlow(data)

    private val openDialogChannel = Channel<ExpenseWithTheme>()
    val openDialogAction = openDialogChannel.receiveAsFlow()

    fun onMarkerClicked(data: ExpenseWithTheme) {
        viewModelScope.launch {
            openDialogChannel.send(data)
        }
    }
}