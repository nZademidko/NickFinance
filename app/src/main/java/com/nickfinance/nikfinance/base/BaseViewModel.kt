package com.nickfinance.nikfinance.base

import androidx.lifecycle.AndroidViewModel
import com.nickfinance.nikfinance.App
import dagger.hilt.android.lifecycle.HiltViewModel

abstract class BaseViewModel() : AndroidViewModel(App.get())