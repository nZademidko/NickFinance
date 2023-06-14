package com.nickfinance.nikfinance.features.add.theme

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.base.ThemeIconState
import com.nickfinance.nikfinance.base.views.builders.*
import com.nickfinance.nikfinance.domain.models.AddThemeData
import com.nickfinance.nikfinance.domain.repositories.abstractions.MainRepository
import com.nickfinance.nikfinance.utils.extensions.getString
import com.nickfinance.nikfinance.utils.extensions.parcelable
import dagger.hilt.android.lifecycle.HiltViewModel
import com.nickfinance.nikfinance.base.executor.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddThemeViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
) : BaseViewModel(application) {

    val setCIconBuilder = MutableStateFlow<ViewSingleLineBuilder?>(null)
    val setCColorBuilder = MutableStateFlow<ViewSingleLineBuilder?>(null)
    val setCThemeCard = MutableStateFlow<AddThemeData?>(null)
    val setButtonEnable = MutableStateFlow(false)
    private val openThemeIconBottomDialogChannel =
        Channel<Pair<Array<ThemeIconState>, ThemeIconState?>>()
    val openThemeIconBottomDialogAction = openThemeIconBottomDialogChannel.receiveAsFlow()
    private val chooseColorChannel = Channel<Unit>()
    val chooseColorAction = chooseColorChannel.receiveAsFlow()
    private var data = AddThemeData()

    init {
        initUi()
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            getString(R.string.key_add_icon_theme) -> {
                data = data.copy(
                    iconState = result.parcelable(getString(R.string.data_add_icon_theme))
                )
                initUi()
            }
        }
    }

    fun onThemeNameChanged(text: String) {
        data = data.copy(name = text)
        initUi()
    }

    fun colorSelected(color: Int) {
        data = data.copy(color = color)
        initUi()
    }

    fun saveTheme() {
        doJob(
            onProgress = {
                mainRepository.saveTheme(data)
            },
            onFinish = { result ->
                when (result) {
                    is Result.Success -> {
                        navigateTo(AddThemeFragmentDirections.toMainPagerFragment())
                    }

                    is Result.Error -> {}
                }
            }
        )
    }

    private fun initUi() {
        checkFills()
        setCIconBuilder.value = ViewSingleLineBuilder.newBuilder()
            .setStateTextLeft(
                TextState.Show(
                    value = "Иконка"
                )
            )
            .setStateRoot(
                RootState.Fill(
                    clickState = ClickState.Action {
                        viewModelScope.launch {
                            openThemeIconBottomDialogChannel.send(
                                Pair(ThemeIconState.values(), data.iconState)
                            )
                        }
                    }
                )
            )
            .setStateTextRight(
                TextState.Show(
                    value = if (data.iconState == null) "Не выбрана" else "Выбрана"
                )
            )
            .setMarginState(
                MarginState.Custom(16, 0, 8, 8)
            )
            .build()

        setCColorBuilder.value = ViewSingleLineBuilder.newBuilder()
            .setStateTextLeft(
                TextState.Show("Цвет иконки")
            )
            .setMarginState(
                MarginState.Custom(16, 0, 8, 8)
            )
            .setStateRoot(
                RootState.Fill(
                    clickState = ClickState.Action {
                        viewModelScope.launch {
                            chooseColorChannel.send(Unit)
                        }
                    }
                )
            )
            .setStateTextRight(
                TextState.Show(
                    value = if (data.color == null) "Не выбран" else "Выбран"
                )
            )
            .build()

        setCThemeCard.value = data
    }

    private fun checkFills() {
        setButtonEnable.value = data.iconState != null
                && data.color != null
                && data.name != null
                && data.name!!.isNotBlank()
    }
}