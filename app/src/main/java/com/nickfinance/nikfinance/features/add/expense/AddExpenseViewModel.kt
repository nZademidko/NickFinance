package com.nickfinance.nikfinance.features.add.expense

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.base.PermissionFragment
import com.nickfinance.nikfinance.base.adapter.BaseRowHolder
import com.nickfinance.nikfinance.base.executor.Result
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.views.builders.*
import com.nickfinance.nikfinance.domain.models.Theme
import com.nickfinance.nikfinance.domain.models.AddExpenseData
import com.nickfinance.nikfinance.domain.repositories.abstractions.MainRepository
import com.nickfinance.nikfinance.features.add.adapter.ThemeWithoutExpenseHolder
import com.nickfinance.nikfinance.utils.DateFormats
import com.nickfinance.nikfinance.utils.extensions.getString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
) : BaseViewModel(application) {

    private val _themesListUiState = MutableStateFlow<ThemesListUiState>(ThemesListUiState.Loading)
    val themesListUiState = _themesListUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = ThemesListUiState.Loading
    )
    private val _cTimeUiState = MutableStateFlow<CTimeUiState>(CTimeUiState.Loading)
    val cTimeUiState = _cTimeUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = CTimeUiState.Loading
    )
    private val _cLocationUiState = MutableStateFlow<CLocationUiState>(CLocationUiState.Loading)
    val cLocationUiState = _cLocationUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = CLocationUiState.Loading
    )
    val buttonState = MutableStateFlow(false)
    private val _selectTimeAction = Channel<Unit>()
    val selectTimeAction = _selectTimeAction.receiveAsFlow()
    private val _openQrAction = Channel<Unit>()
    val openQrAction = _openQrAction.receiveAsFlow()

    private var addExpenseData = MutableStateFlow(AddExpenseData())

    init {
        viewModelScope.launch {
            addExpenseData.collect {
                checkFills()
            }
        }
        getThemes()
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            getString(R.string.key_location) -> {
                addExpenseData.value = addExpenseData.value.copy(
                    location = result.getString(getString(R.string.data_location))
                )
                initLocationUi()
            }
        }
    }

    private fun getThemes() {
        doFlowJob(
            onProgress = {
                mainRepository.getThemes()
            },
            onFinish = { result ->
                when (result) {
                    is Result.Success -> {
                        initUi(result.data)
                    }

                    is Result.Error -> {}
                }
            }
        )
    }

    fun setTime(time: Long) {
        addExpenseData.value = addExpenseData.value.copy(createdAt = Date(time))
        initTimeUi()
    }

    fun setAmount(amount: Double) {
        addExpenseData.value = addExpenseData.value.copy(amount = amount.toString())
    }

    fun onAmountTextChanged(text: String) {
        addExpenseData.value = addExpenseData.value.copy(amount = text)
    }

    fun onCommentTextChanged(text: String) {
        addExpenseData.value = addExpenseData.value.copy(textComment = text)
    }

    fun save() {
        doJob(
            onProgress = {
                mainRepository.saveExpense(data = addExpenseData.value)
            },
            onFinish = {
                navigateTo(AddExpenseFragmentDirections.toMainPagerFragment())
            }
        )
    }

    fun checkPermission() {
        requestPermission(
            object : PermissionFragment.RequestPermissions() {
                override fun failGetPermissions(list: List<PermissionFragment.PermissionType>) {
                    list.getOrNull(0)?.let { showSnackBar(it.textDenied) }
                }

                override fun successGetPermissions(
                    list: List<PermissionFragment.PermissionType>,
                    isAllGranted: Boolean
                ) {
                    viewModelScope.launch {
                        _openQrAction.send(Unit)
                    }
                }
            },
            listOf(PermissionFragment.PermissionType.CAMERA)
        )
    }

    private fun checkFills() {
        with(addExpenseData.value) {
            buttonState.value = !amount.isNullOrBlank() && theme != null
        }
    }

    private fun initUi(themes: List<Theme>) {
        _themesListUiState.value = ThemesListUiState.Success(
            data = themes.map { theme ->
                val bgColor: ColorState
                val textColor: ColorState
                if (theme == addExpenseData.value.theme) {
                    bgColor =
                        ColorState.FromResource(R.color.md_theme_light_primary)
                    textColor = ColorState.FromResource(R.color.white)
                } else {
                    bgColor =
                        ColorState.FromResource(R.color.transparent)
                    textColor = ColorState.FromIntColor(theme.color)
                }
                ThemeWithoutExpenseHolder(
                    builder = ViewThemeWithoutExpenseBuilder.newBuilder()
                        .setThemeState(
                            TextState.Show(
                                colorState = textColor,
                                styleId = R.style.TextAppearance_App_Body_B1_text_accent_16,
                                value = theme.name
                            )
                        )
                        .setIconState(
                            IconState.ShowFromRes(
                                iconId = theme.iconId,
                                colorState = ColorState.FromIntColor(theme.color)
                            )
                        )
                        .setRadiusType(RadiusState.RADIUS_CIRCLE)
                        .setStateRoot(
                            RootState.Fill(
                                colorState = bgColor,
                                clickState = ClickState.Action {
                                    addExpenseData.value =
                                        addExpenseData.value.copy(theme = theme)
                                    getThemes()
                                }
                            )
                        )
                        .build()
                )
            }
        )
        initTimeUi()
        initLocationUi()
    }

    private fun initTimeUi() {
        _cTimeUiState.value = CTimeUiState.Success(
            builder = ViewSingleLineBuilder.newBuilder()
                .setStateTextLeft(
                    TextState.Show(
                        value = "Время траты"
                    )
                )
                .setStateIconRight(
                    IconState.ShowFromRes(
                        iconId = R.drawable.ic_calendar,
                        colorState = ColorState.FromResource(R.color.md_theme_light_primary)
                    )
                )
                .setMarginState(
                    MarginState.Custom(top = 8, bottom = 4, left = 0, right = 0)
                )
                .setStateTextRight(
                    TextState.Show(
                        value = DateFormats.getString(
                            addExpenseData.value.createdAt,
                            DateFormats.Companion.Format.DD_MM_YYYY_HH_MM
                        )
                    )
                )
                .setStateRoot(
                    RootState.Fill(
                        clickState = ClickState.Action {
                            viewModelScope.launch {
                                _selectTimeAction.send(Unit)
                            }
                        }
                    )
                )
                .build()
        )
    }

    private fun initLocationUi() {
        _cLocationUiState.value = CLocationUiState.Success(
            builder = ViewSingleLineBuilder.newBuilder()
                .setStateTextLeft(
                    TextState.Show(
                        value = "Локация"
                    )
                )
                .setStateIconRight(
                    IconState.ShowFromRes(
                        iconId = R.drawable.ic_map,
                        colorState = ColorState.FromResource(R.color.md_theme_light_primary)
                    )
                )
                .setMarginState(
                    MarginState.Custom(top = 8, bottom = 4, left = 0, right = 0)
                )
                .setStateTextRight(
                    TextState.Show(
                        if (addExpenseData.value.location == null) {
                            "Не добавлена"
                        } else {
                            "Добавлена"
                        }
                    )
                )
                .setStateRoot(
                    RootState.Fill(
                        clickState = ClickState.Action {
                            setLocation()
                        }
                    )
                )
                .build()
        )
    }

    private fun setLocation() {
        requestPermission(
            object : PermissionFragment.RequestPermissions() {
                override fun failGetPermissions(list: List<PermissionFragment.PermissionType>) {
                    showSnackBar(list[0].textDenied)
                }

                override fun successGetPermissions(
                    list: List<PermissionFragment.PermissionType>,
                    isAllGranted: Boolean
                ) {
                    if (isAllGranted) {
                        navigateTo(
                            AddExpenseFragmentDirections.toChooseLocationFragment(
                                addExpenseData.value.location
                            )
                        )
                    }
                }
            },
            listOf(
                PermissionFragment.PermissionType.ACCESS_FINE_LOCATION,
                PermissionFragment.PermissionType.ACCESS_COARSE_LOCATION
            )
        )
    }

    sealed class ThemesListUiState {

        object Loading : ThemesListUiState()

        class Success(val data: List<BaseRowHolder>) : ThemesListUiState()

    }

    sealed class CTimeUiState {

        object Loading : CTimeUiState()

        class Success(val builder: ViewSingleLineBuilder) : CTimeUiState()

    }

    sealed class CLocationUiState {

        object Loading : CLocationUiState()

        class Success(val builder: ViewSingleLineBuilder) : CLocationUiState()

    }
}