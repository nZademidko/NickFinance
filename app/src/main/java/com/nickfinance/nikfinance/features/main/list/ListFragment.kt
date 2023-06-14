package com.nickfinance.nikfinance.features.main.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.nickfinance.nikfinance.base.BaseFragment
import com.nickfinance.nikfinance.base.adapter.BaseRowHolderAdapter
import com.nickfinance.nikfinance.base.adapter.holders.adapterSingleLineDelegate
import com.nickfinance.nikfinance.base.adapter.holders.adapterTimeDelegate
import com.nickfinance.nikfinance.databinding.FragmentListBinding
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : BaseFragment<ListViewModel, FragmentListBinding>() {

    private lateinit var adapter: BaseRowHolderAdapter

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BaseRowHolderAdapter(
            adapterSingleLineDelegate(),
            adapterTimeDelegate()
        )

        vb.rvExpanses.adapter = adapter
    }

    fun refreshData(data: List<ExpenseWithTheme>) {
        vm.initList(data)
    }

    override fun initCustomObservers() {
        vm.items.observe { state ->
            when (state) {
                is ListViewModel.ListUiState.Loading -> {
                    vb.lProgress.root.visibility = View.VISIBLE
                    vb.lEmptyList.root.visibility = View.GONE
                }

                is ListViewModel.ListUiState.Success -> {
                    when (state.data.isEmpty()) {
                        true -> {
                            vb.lProgress.root.visibility = View.GONE
                            vb.lEmptyList.root.visibility = View.VISIBLE
                        }
                        false -> {
                            vb.lProgress.root.visibility = View.GONE
                            vb.lEmptyList.root.visibility = View.GONE
                            adapter.update(state.data)
                        }
                    }
                }
            }
        }
    }
}