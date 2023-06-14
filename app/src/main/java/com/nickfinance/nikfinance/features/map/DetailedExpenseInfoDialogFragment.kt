package com.nickfinance.nikfinance.features.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.imageLoader.Type
import com.nickfinance.nikfinance.databinding.BottomSheetDetailedMapExpenseBinding
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.utils.DateFormats
import com.nickfinance.nikfinance.utils.extensions.loader
import com.nickfinance.nikfinance.utils.extensions.parcelable
import java.util.*

class DetailedExpenseInfoDialogFragment : BottomSheetDialogFragment() {

    private lateinit var vb: BottomSheetDetailedMapExpenseBinding
    private var data: ExpenseWithTheme? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        vb = BottomSheetDetailedMapExpenseBinding.inflate(inflater)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = requireArguments().parcelable(Companion.data)
        initUi(data!!)
    }

    private fun initUi(data: ExpenseWithTheme) {
        vb.ivTheme.loader(
            Type.FromRes(
                resId = data.theme.iconId,
                colorState = ColorState.FromIntColor(data.theme.color)
            )
        )

        vb.tvTheme.text = data.theme.name
        vb.tvTheme.setTextColor(data.theme.color)

        vb.mcvContainer.strokeColor = data.theme.color

        vb.tvAmount.text = data.expense.amount.toString()
        vb.tvTime.text = DateFormats.getString(
            Date(data.expense.createdAt),
            DateFormats.Companion.Format.DD_MM_YYYY_HH_MM
        )
    }

    companion object {
        private const val data = "DATA"

        fun openDialog(
            parent: Fragment, expenseWithTheme: ExpenseWithTheme
        ): DetailedExpenseInfoDialogFragment {
            val dialog = DetailedExpenseInfoDialogFragment()
            dialog.arguments = bundleOf(data to expenseWithTheme)
            dialog.show(parent.parentFragmentManager, null)
            return dialog
        }
    }
}