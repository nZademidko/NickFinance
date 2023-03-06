package com.nickfinance.nikfinance.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment() {

    @Suppress("UNCHECKED_CAST")
    protected val vb: VB
        get() = _binding as VB
    protected lateinit var vm: VM
    private var _binding: ViewBinding? = null
    private var container: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = createViewModelInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCustomObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.container = FrameLayout(requireContext())
        _binding = createViewBindingInstance(inflater, container)
        this.container?.addView(requireNotNull(_binding).root)
        return this.container!!
    }

    abstract fun initCustomObservers()

    protected fun navigateTo(direction: NavDirections) {
        findNavController().navigate(direction)
    }

    @Suppress("UNCHECKED_CAST")
    private fun createViewBindingInstance(inflater: LayoutInflater, container: ViewGroup?): VB {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val vbClass = parameterizedType.actualTypeArguments.getOrNull(1) as Class<VB>
        val method = vbClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return method.invoke(null, inflater, container, false) as VB
    }

    @Suppress("UNCHECKED_CAST")
    private fun createViewModelInstance(): VM {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val vmClass = parameterizedType.actualTypeArguments.getOrNull(0) as Class<VM>
        return ViewModelProvider(this)[vmClass]
    }

    protected inline fun <T> StateFlow<T>.observe(crossinline observer: (T) -> Unit) =
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@observe.collect { data ->
                    observer(data)
                }
            }
        }

    protected inline fun <T> SharedFlow<T>.observe(crossinline observer: (T) -> Unit) =
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@observe.collect { data ->
                    observer(data)
                }
            }
        }
}