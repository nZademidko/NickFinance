package com.nickfinance.nikfinance.features.main.pager

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.BaseFragment
import com.nickfinance.nikfinance.base.MessageReceiver
import com.nickfinance.nikfinance.databinding.FragmentMainPagerBinding
import com.nickfinance.nikfinance.features.main.FilterDialogFragment
import com.nickfinance.nikfinance.features.main.MainTab
import com.nickfinance.nikfinance.features.main.chart.ChartFragment
import com.nickfinance.nikfinance.features.main.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPagerFragment : BaseFragment<MainPagerViewModel, FragmentMainPagerBinding>() {

    private var listFragment: ListFragment? = null
    private var chartFragment: ChartFragment? = null
    private lateinit var adapter: ViewStateAdapter

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.rotate_fab_open_anim)
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.rotate_fab_close_anim)
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim)
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim)
    }

    private var fabClicked = false

    override fun resIdsRequestKey(): List<Int> {
        return listOf(
            R.string.key_filter
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerPermissions()
        fabClicked = false
        if (savedInstanceState != null) {
            listFragment = childFragmentManager.findFragmentByTag("f0") as ListFragment?
            chartFragment = childFragmentManager.findFragmentByTag("f1") as ChartFragment?
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = MainTab.values()
        adapter = ViewStateAdapter(this, list)
        vb.vpPager.adapter = adapter
        vb.vpPager.offscreenPageLimit = 2
        vb.vpPager.isUserInputEnabled = false
        TabLayoutMediator(vb.tabLayout, vb.vpPager) { tab, position ->
            tab.text = getString(list[position].title)
        }.attach()

        vb.fabAddGeneral.setOnClickListener {
            onAddButtonClicked()
        }

        vb.fabAddExpense.setOnClickListener {
            vm.toAddExpense()
        }

        vb.fabAddTheme.setOnClickListener {
            vm.toAddTheme()
        }

        vb.btnFilter.setOnClickListener {
            vm.onFilterClicked()
        }

        vb.tbMain.setNavigationOnClickListener {
            vb.drawerLayout.open()
        }

        vb.nvMain.setNavigationItemSelectedListener { item ->
            vb.drawerLayout.close()
            when (item.itemId) {
                R.id.expense_map_item -> {
                    vm.toExpensesMap()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun initCustomObservers() {
        vm.mainPagerUiState.observe { state ->
            when (state) {
                is MainPagerViewModel.MainPagerUiState.Loading -> {
                    vb.lProgress.root.visibility = View.VISIBLE
                }

                is MainPagerViewModel.MainPagerUiState.Success -> {
                    vb.lProgress.root.visibility = View.GONE
                    listFragment?.refreshData(state.data)
                    chartFragment?.refreshData(state.data)
                }
            }
        }
        vm.chooseFilterAction.observe { data ->
            FilterDialogFragment.openDialog(this@MainPagerFragment, data)
        }
    }

    override fun onPause() {
        super.onPause()
        fabClicked = false
    }

    override fun onDestroy() {
        super.onDestroy()
        fabClicked = false
    }

    private fun onAddButtonClicked() {
        if (!fabClicked) {
            vb.fabAddExpense.visibility = View.VISIBLE
            vb.fabAddTheme.visibility = View.VISIBLE
            vb.fabAddExpense.startAnimation(fromBottom)
            vb.fabAddTheme.startAnimation(fromBottom)
            vb.fabAddGeneral.startAnimation(rotateOpen)
        } else {
            vb.fabAddExpense.visibility = View.INVISIBLE
            vb.fabAddTheme.visibility = View.INVISIBLE
            vb.fabAddExpense.startAnimation(toBottom)
            vb.fabAddTheme.startAnimation(toBottom)
            vb.fabAddGeneral.startAnimation(rotateClose)
        }
        fabClicked = !fabClicked
    }

    private class ViewStateAdapter(val fragment: MainPagerFragment, val tabs: Array<MainTab>) :
        FragmentStateAdapter(fragment) {

        override fun createFragment(position: Int): Fragment {
            return when (tabs[position]) {
                MainTab.LIST -> {
                    fragment.listFragment = ListFragment()
                    fragment.listFragment!!
                }
                MainTab.CHART -> {
                    fragment.chartFragment = ChartFragment()
                    fragment.chartFragment!!
                }
            }
        }

        override fun getItemCount(): Int {
            return tabs.size
        }
    }
}