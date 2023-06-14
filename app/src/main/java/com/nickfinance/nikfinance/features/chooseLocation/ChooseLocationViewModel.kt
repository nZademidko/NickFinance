package com.nickfinance.nikfinance.features.chooseLocation

import android.app.Application
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.BaseViewModel
import com.nickfinance.nikfinance.utils.extensions.getString
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ChooseLocationViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {

    private val location =
        ChooseLocationFragmentArgs.fromSavedStateHandle(savedStateHandle).location
    val locationState = MutableStateFlow(location)

    fun onLocationClicked(p: Point) {
        setFragmentResult(
            key = R.string.key_location,
            bundle = bundleOf(getString(R.string.data_location) to "${p.latitude};${p.longitude}")
        )
        onBackPressed()
    }
}