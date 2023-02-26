package com.nickfinance.nikfinance.features.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import com.nickfinance.nikfinance.base.BaseFragment
import com.nickfinance.nikfinance.base.BaseRowHolderAdapter
import com.nickfinance.nikfinance.databinding.FragmentMainBinding
import com.nickfinance.nikfinance.features.add.adapter.adapterThemesDelegate
import com.nickfinance.nikfinance.features.main.adapter.adapterMainListExpensesDelegate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val vm by viewModels<MainViewModel>()

    private lateinit var adapter: BaseRowHolderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BaseRowHolderAdapter(
            adapterMainListExpensesDelegate()
        )

        vb.rvExpanses.adapter = adapter

        vb.bAdd.setOnClickListener {
            navigateTo(MainFragmentDirections.toAddExpense())
        }
    }

    override fun initCustomObservers() {
        vm.mainUiState.observe { state ->
            when (state) {
                is MainViewModel.MainUiState.Loading -> {
                    vb.overlapEmptyExpanses.visibility = View.GONE
                    vb.rvExpanses.visibility = View.GONE
                    vb.pbLoading.visibility = View.VISIBLE
                }

                is MainViewModel.MainUiState.Success -> {
                    when (state.data.isEmpty()) {
                        true -> {
                            vb.overlapEmptyExpanses.visibility = View.VISIBLE
                            vb.rvExpanses.visibility = View.GONE
                            vb.pbLoading.visibility = View.GONE
                        }
                        false -> {
                            vb.overlapEmptyExpanses.visibility = View.GONE
                            vb.rvExpanses.visibility = View.VISIBLE
                            vb.pbLoading.visibility = View.GONE
                            adapter.update(state.data)
                        }
                    }
                }
            }
        }
    }
}