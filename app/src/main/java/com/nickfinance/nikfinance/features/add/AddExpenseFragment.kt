package com.nickfinance.nikfinance.features.add

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.nickfinance.nikfinance.base.BaseFragment
import com.nickfinance.nikfinance.base.BaseRowHolderAdapter
import com.nickfinance.nikfinance.databinding.FragmentAddExpenseBinding
import com.nickfinance.nikfinance.features.add.adapter.adapterThemesDelegate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExpenseFragment : BaseFragment<AddExpenseViewModel, FragmentAddExpenseBinding>() {

    private lateinit var adapter: BaseRowHolderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BaseRowHolderAdapter(
            adapterThemesDelegate()
        )

        vb.rvThemes.adapter = adapter

        vb.etAmount.doOnTextChanged { text, _, _, _ ->
            vm.onAmountTextChanged(text.toString())
        }

        vb.etComment.doOnTextChanged { text, _, _, _ ->
            vm.onCommentTextChanged(text.toString())
        }

        vb.bContinue.setOnClickListener {
            vm.save()
        }

    }

    override fun initCustomObservers() {
        vm.themesUiState.observe { state ->
            when (state) {
                is AddExpenseViewModel.ThemesUiState.Success -> {
                    adapter.update(items = state.data)
                }
                is AddExpenseViewModel.ThemesUiState.Loading -> {}
            }
        }

        vm.buttonState.observe { state -> vb.bContinue.isEnabled = state }

        vm.navigateState.observe {
            navigateTo(it)
        }
    }
}