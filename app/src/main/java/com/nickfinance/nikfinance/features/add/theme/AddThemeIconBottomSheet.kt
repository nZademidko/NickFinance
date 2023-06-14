package com.nickfinance.nikfinance.features.add.theme

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.ThemeIconState
import com.nickfinance.nikfinance.base.adapter.BaseRowHolderAdapter
import com.nickfinance.nikfinance.features.add.adapter.ThemeIconsHolder
import com.nickfinance.nikfinance.features.add.adapter.adapterThemeIconsDelegate
import com.nickfinance.nikfinance.utils.extensions.parcelable
import kotlinx.parcelize.Parcelize

class AddThemeIconBottomSheet : BottomSheetDialogFragment() {

    private lateinit var rv: RecyclerView
    private lateinit var root: LinearLayout
    private var selectedIconState: ThemeIconState? = null
    private var data: ThemeIconBottomSheetData? = null

    private val adapter = BaseRowHolderAdapter(
        adapterThemeIconsDelegate()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_add_icon_theme, container, false)
        rv = view.findViewById(R.id.rvIcons)
        root = view.findViewById(R.id.standard_bottom_sheet)
        rv.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = requireArguments().parcelable(Companion.data)
        selectedIconState = data?.selectedIcon
        updateAdapter()
    }

    private fun updateAdapter() {
        val bottomSheetDialog = dialog as BottomSheetDialog
        bottomSheetDialog.dismissWithAnimation = false
        bottomSheetDialog.behavior.apply {
            isHideable = true
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        adapter.update(data!!.iconsList.map { state ->
            ThemeIconsHolder(
                iconState = state,
                selected = state == selectedIconState,
                onClicked = {
                    setFragmentResult(
                        getString(R.string.key_add_icon_theme),
                        bundleOf(getString(R.string.data_add_icon_theme) to state)
                    )
                    dismiss()
                }
            )
        })
    }

    companion object {
        private const val data = "DATA"

        fun openDialog(
            parent: Fragment, list: List<ThemeIconState>, selectedIcon: ThemeIconState? = null
        ): AddThemeIconBottomSheet {
            val dialog = AddThemeIconBottomSheet()
            dialog.arguments = bundleOf(data to ThemeIconBottomSheetData(list, selectedIcon))
            dialog.show(parent.parentFragmentManager, null)
            return dialog
        }
    }

    @Parcelize
    data class ThemeIconBottomSheetData(
        val iconsList: List<ThemeIconState>,
        val selectedIcon: ThemeIconState? = null
    ) : Parcelable
}