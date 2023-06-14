package com.nickfinance.nikfinance.features.detailedExpense

import android.os.Bundle
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.BaseFragment
import com.nickfinance.nikfinance.databinding.FragmentDetailedExpenseBinding
import com.nickfinance.nikfinance.utils.DateFormats
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DetailedExpenseFragment :
    BaseFragment<DetailedExpenseViewModel, FragmentDetailedExpenseBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.toolBar.setNavigationOnClickListener {
            vm.onBackPressed()
        }
        vb.toolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.map -> {
                    vm.onMapMenuClicked()
                    true
                }
                else -> false
            }
        }
        vb.btnArchive.setOnClickListener {
            vm.archiveExpense()
        }
        vb.btnDelete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Подтвердите действие!")
                .setMessage("Вы точно хотите удалить трату?")
                .setPositiveButton("Удалить") { _, _ ->
                    vm.deleteExpense()
                }
                .setNegativeButton("Отмена") { _, _ -> }
                .show()
        }
    }

    override fun initCustomObservers() {
        vm.detailedExpenseUiState.observe { state ->
            when (state) {
                is DetailedExpenseViewModel.DetailedExpenseUiState.Loading -> {}

                is DetailedExpenseViewModel.DetailedExpenseUiState.Success -> {
                    val data = state.data
                    vb.etAmount.setText(data.expense.amount.toString())
                    vb.etComment.setText(data.expense.commentText ?: "Комментарий отсутствует")
                    vb.etTheme.setText(data.theme.name)
                    vb.etCreatedAt.setText(
                        DateFormats.getString(
                            Date(data.expense.createdAt),
                            DateFormats.Companion.Format.DD_MM_YYYY_HH_MM
                        )
                    )
                }
            }
        }
        vm.btnArchiveStateAction.observe { text ->
            vb.btnArchive.text = text
        }
    }
}