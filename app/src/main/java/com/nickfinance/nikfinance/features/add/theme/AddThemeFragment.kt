package com.nickfinance.nikfinance.features.add.theme

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.BaseFragment
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.imageLoader.Type
import com.nickfinance.nikfinance.databinding.FragmentAddThemeBinding
import com.nickfinance.nikfinance.utils.extensions.loader
import dagger.hilt.android.AndroidEntryPoint
import yuku.ambilwarna.AmbilWarnaDialog

@AndroidEntryPoint
class AddThemeFragment : BaseFragment<AddThemeViewModel, FragmentAddThemeBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vb.toolBar.setNavigationOnClickListener {
            vm.onBackPressed()
        }

        vb.etAmount.doOnTextChanged { text, _, _, _ ->
            vm.onThemeNameChanged(text?.toString() ?: "")
        }

        vb.btnSave.setOnClickListener {
            vm.saveTheme()
        }
    }

    override fun resIdsRequestKey(): List<Int> {
        return listOf(R.string.key_add_icon_theme)
    }

    override fun initCustomObservers() {
        vm.setCIconBuilder.observe { builder ->
            builder?.let { vb.cIcon.initUI(it) }
        }
        vm.setCColorBuilder.observe { builder ->
            builder?.let { vb.cColor.initUI(it) }
        }
        vm.openThemeIconBottomDialogAction.observe { pair ->
            AddThemeIconBottomSheet.openDialog(
                this@AddThemeFragment,
                list = pair.first.toList(),
                selectedIcon = pair.second
            )
        }
        vm.setCThemeCard.observe { data ->
            data?.let {
                it.iconState?.let { themeIconState ->
                    vb.ivIconTheme.loader(
                        Type.FromRes(
                            resId = themeIconState.resId,
                            colorState = it.color?.let { color ->
                                ColorState.FromIntColor(color)
                            } ?: ColorState.FromResource(R.color.black10)
                        )
                    )
                }

                it.name?.let { text ->
                    vb.tvNameTheme.text = text
                    vb.tvNameTheme.setTextColor(it.color ?: R.color.black10)
                }
            }
        }
        vm.chooseColorAction.observe {
            AmbilWarnaDialog(
                requireContext(), 0, object : AmbilWarnaDialog.OnAmbilWarnaListener {
                    override fun onCancel(dialog: AmbilWarnaDialog?) {}

                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        vm.colorSelected(color)
                    }
                }
            ).show()
        }
        vm.setButtonEnable.observe { vb.btnSave.isEnabled = it }
    }
}