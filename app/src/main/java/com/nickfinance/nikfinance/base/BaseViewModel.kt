package com.nickfinance.nikfinance.base

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.nickfinance.nikfinance.base.executor.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow

open class BaseViewModel(application: Application) : AndroidViewModel(application),
    FragmentResultListener {

    private val navigationAction = Channel<NavigationAction>()
    val _navigationAction = navigationAction.receiveAsFlow()

    private val fragmentResultAction = Channel<Pair<Int, Bundle>>()
    val _fragmentResultAction = fragmentResultAction.receiveAsFlow()

    private val requestPermissionChannel =
        Channel<Pair<PermissionFragment.RequestPermissions, List<PermissionFragment.PermissionType>>>()
    val _requestPermissionAction = requestPermissionChannel.receiveAsFlow()

    private val showSnackBarChannel = Channel<CharSequence>()
    val _showSnackBarAction = showSnackBarChannel.receiveAsFlow()

    fun onBackPressed() = viewModelScope.launch {
        navigationAction.send(NavigationAction.NavigateBack)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {}

    protected fun navigateTo(nav: NavDirections) = viewModelScope.launch {
        navigationAction.send(NavigationAction.NavigateTo(nav))
    }

    protected fun requestPermission(
        reqPermissions: PermissionFragment.RequestPermissions,
        list: List<PermissionFragment.PermissionType>
    ) = viewModelScope.launch {
        requestPermissionChannel.send(Pair(reqPermissions, list))
    }

    protected fun showSnackBar(text: CharSequence) = viewModelScope.launch {
        showSnackBarChannel.send(text)
    }

    protected fun setFragmentResult(key: Int, bundle: Bundle) = viewModelScope.launch {
        fragmentResultAction.send(Pair(key, bundle))
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
        crossinline onProgress: suspend CoroutineScope.() -> Flow<Model>,
        crossinline onFinish: (result: Result<Model>) -> Unit
    ) {
        onLoading.invoke()
        viewModelScope.launch {
            withContext(Dispatchers.IO) { onProgress.invoke(this) }
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
        class NavigateTo(val navDirections: NavDirections) : NavigationAction()
        object NavigateBack : NavigationAction()
    }
}