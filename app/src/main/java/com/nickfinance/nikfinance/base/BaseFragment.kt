package com.nickfinance.nikfinance.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.nickfinance.nikfinance.utils.FragmentResultListener
import com.yandex.metrica.impl.ob.Ch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : PermissionFragment(),
    FragmentResultListener {

    @Suppress("UNCHECKED_CAST")
    protected val vb: VB
        get() = _binding as VB
    protected lateinit var vm: VM
    private var _binding: ViewBinding? = null
    private var container: FrameLayout? = null
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = createViewModelInstance()

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                vm.onBackPressed()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        resIdsRequestKey().forEach { resId ->
            clearFragmentResultListener(getString(resId))
            setFragmentResultListener(requestKey = getString(resId)) { key, bundle ->
                vm.onFragmentResult(key, bundle)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBaseObservers()
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

    override fun resIdsRequestKey() = listOf<Int>()

    override fun resIdsRequestKeyForViewPager() = listOf<Int>()

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
    }

    abstract fun initCustomObservers()

    private fun initBaseObservers() {
        vm._navigationAction.observe { action ->
            when (action) {
                is BaseViewModel.NavigationAction.NavigateTo -> {
                    findNavController().navigate(action.navDirections)
                }

                is BaseViewModel.NavigationAction.NavigateBack -> {
                    if (!findNavController().popBackStack()) {
                        requireActivity().finish()
                    }
                }
            }
        }

        vm._fragmentResultAction.observe { action ->
            setFragmentResult(
                getString(action.first),
                action.second
            )
        }

        vm._requestPermissionAction.observe { pair ->
            requestPermissions(pair.first, pair.second)
        }

        vm._showSnackBarAction.observe { text ->
            showSnackBar(text)
        }
    }

    private fun showSnackBar(text: CharSequence) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@observe.collect { data ->
                    observer(data)
                }
            }
        }

    protected inline fun <T> SharedFlow<T>.observe(crossinline observer: (T) -> Unit) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@observe.collect { data ->
                    observer(data)
                }
            }
        }

    protected inline fun <T> Flow<T>.observe(crossinline observer: (T) -> Unit) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@observe.collect { data ->
                    observer(data)
                }
            }
        }
}