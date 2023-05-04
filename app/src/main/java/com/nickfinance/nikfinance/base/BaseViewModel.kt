package com.nickfinance.nikfinance.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.nickfinance.nikfinance.base.executor.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val navigationAction = Channel<NavigationAction>()
    val _navigationAction = navigationAction.receiveAsFlow()

    fun onBackPressed() = viewModelScope.launch {
        navigationAction.send(NavigationAction.navigateBack)
    }

    protected inline fun <reified Model> doJob(
        crossinline onLoading: () -> Unit = {},
        crossinline onProgress: suspend CoroutineScope.() -> Model,
        crossinline onFinish: (result: Result<Model>) -> Unit
    ) {
        viewModelScope.launch {
            onLoading.invoke()
            try {
                val model: Deferred<Model> = async(Dispatchers.IO) { onProgress.invoke(this) }
                onFinish.invoke(Result.Success(model.await()))
            } catch (error: Exception) {
                onFinish.invoke(Result.Error(error.message ?: ""))
            }
        }
    }

    protected inline fun <reified Model> doFlowJob(
        crossinline onLoading: () -> Unit = {},
        crossinline onProgress: () -> Flow<Model>,
        crossinline onFinish: (result: Result<Model>) -> Unit
    ) {
        onLoading.invoke()
        viewModelScope.launch {
            withContext(Dispatchers.IO) { onProgress.invoke() }
                .catch { it ->
                    if (it is java.lang.IllegalStateException) {
                        throw java.lang.IllegalStateException(it.message)
                    } else {
                        onFinish.invoke(Result.Error(it.message ?: ""))
                    }
                }
                .collect { onFinish.invoke(Result.Success(it)) }
        }
    }

    sealed class NavigationAction {
        class navigateTo(val navDirections: NavDirections) : NavigationAction()
        object navigateBack : NavigationAction()
    }
}