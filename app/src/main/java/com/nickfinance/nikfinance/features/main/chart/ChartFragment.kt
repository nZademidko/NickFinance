package com.nickfinance.nikfinance.features.main.chart

import android.os.Bundle
import android.view.View
import com.nickfinance.nikfinance.base.BaseFragment
import com.nickfinance.nikfinance.base.adapter.BaseRowHolderAdapter
import com.nickfinance.nikfinance.base.adapter.holders.adapterThemeDelegate
import com.nickfinance.nikfinance.databinding.FragmentChartBinding
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChartFragment : BaseFragment<ChartViewModel, FragmentChartBinding>() {

    private lateinit var firstThemeAdapter: BaseRowHolderAdapter
    private lateinit var secondThemeAdapter: BaseRowHolderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstThemeAdapter = BaseRowHolderAdapter(
            adapterThemeDelegate()
        )

        secondThemeAdapter = BaseRowHolderAdapter(
            adapterThemeDelegate()
        )

        vb.rvFirstThemes.adapter = firstThemeAdapter
        vb.rvSecondThemes.adapter = secondThemeAdapter
    }

    override fun initCustomObservers() {
        vm.chartStateBuilder.observe { state ->
            when (state) {
                is ChartViewModel.ChartUiState.Loading -> {}

                is ChartViewModel.ChartUiState.Success -> {
                    vb.cChart.initUi(state.builder)
                }
            }
        }

        vm.themes.observe { state ->
            when (state) {
                is ChartViewModel.ThemesUiState.Loading -> {
                    vb.pbFirst.visibility = View.VISIBLE
                    vb.pbSecond.visibility = View.VISIBLE
                }

                is ChartViewModel.ThemesUiState.Success -> {
                    vb.pbFirst.visibility = View.GONE
                    vb.pbSecond.visibility = View.GONE

                    vb.rvFirstThemes.visibility =
                        if (state.firstThemes.isEmpty()) View.GONE else View.VISIBLE

                    vb.rvSecondThemes.visibility =
                        if (state.secondThemes.isEmpty()) View.GONE else View.VISIBLE

                    firstThemeAdapter.update(state.firstThemes)
                    secondThemeAdapter.update(state.secondThemes)
                }
            }
        }
    }

    fun refreshData(data: List<ExpenseWithTheme>) {
        vm.initList(data)
    }

}