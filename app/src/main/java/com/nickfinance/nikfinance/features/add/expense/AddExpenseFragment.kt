package com.nickfinance.nikfinance.features.add.expense

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.BaseFragment
import com.nickfinance.nikfinance.base.PermissionFragment
import com.nickfinance.nikfinance.base.ScanActivity
import com.nickfinance.nikfinance.base.adapter.BaseRowHolderAdapter
import com.nickfinance.nikfinance.databinding.FragmentAddExpenseBinding
import com.nickfinance.nikfinance.features.add.adapter.adapterThemeWithoutExpenseDelegate
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddExpenseFragment : BaseFragment<AddExpenseViewModel, FragmentAddExpenseBinding>() {

    private lateinit var adapter: BaseRowHolderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerPermissions()
    }

    override fun resIdsRequestKey(): List<Int> {
        return listOf(R.string.key_location)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vb.toolBar.setNavigationOnClickListener {
            vm.onBackPressed()
        }

        adapter = BaseRowHolderAdapter(
            adapterThemeWithoutExpenseDelegate()
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

        vb.btnScanQR.setOnClickListener {
            vm.checkPermission()
        }
    }

    override fun initCustomObservers() {
        vm.themesListUiState.observe { state ->
            when (state) {
                is AddExpenseViewModel.ThemesListUiState.Success -> {
                    adapter.update(items = state.data)
                }
                is AddExpenseViewModel.ThemesListUiState.Loading -> {}
            }
        }

        vm.cTimeUiState.observe { state ->
            when (state) {
                is AddExpenseViewModel.CTimeUiState.Success -> {
                    vb.cTime.initUI(state.builder)
                }
                is AddExpenseViewModel.CTimeUiState.Loading -> {}
            }

        }

        vm.selectTimeAction.observe {
            val calendar = Calendar.getInstance()
            val currentDate = MaterialDatePicker.todayInUtcMilliseconds()
            calendar.timeInMillis = currentDate

            val constraintsBuilder = CalendarConstraints.Builder().setValidator(
                DateValidatorPointBackward.before(calendar.timeInMillis)
            )
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setNegativeButtonText("Отмена")
                .setPositiveButtonText("Ок")
                .setTitleText("Выберите дату")
                .build()

            datePicker.show(this@AddExpenseFragment.childFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener { dateTime ->

                val cal = Calendar.getInstance()
                val timePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(cal.get(Calendar.HOUR_OF_DAY))
                    .setMinute(cal.get(Calendar.MINUTE))
                    .setNegativeButtonText("Отмена")
                    .setPositiveButtonText("Ок")
                    .build()

                timePicker.show(this@AddExpenseFragment.childFragmentManager, "TimePicker")

                timePicker.addOnPositiveButtonClickListener {
                    val millies =
                        (timePicker.hour * 3_600_000L + timePicker.minute * 60_000L) - TimeZone.getDefault().rawOffset
                    vm.setTime(dateTime + millies)
                }

            }
        }

        vm.cLocationUiState.observe { state ->
            when (state) {
                is AddExpenseViewModel.CLocationUiState.Success -> {
                    vb.cLocation.initUI(state.builder)
                }
                is AddExpenseViewModel.CLocationUiState.Loading -> {}
            }
        }

        vm.buttonState.observe { state -> vb.bContinue.isEnabled = state }

        vm.openQrAction.observe {
            openScanner()
        }
    }

    private fun openScanner() {
        val options = ScanOptions()
        options.setPrompt("Отсканируйте qr")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.captureActivity = ScanActivity::class.java
        launcher.launch(options)
    }

    private val launcher = registerForActivityResult(ScanContract()) { result ->
        try {
            val amount = result.contents.split('s')[1]
                .split('&')[0]
                .replace("=", "")
                .toDouble()
            vb.etAmount.setText(amount.toString())
            vm.setAmount(amount)
        } catch (e: java.lang.Exception) {
            Snackbar.make(requireView(), "Не удалось распознать QR-код", Snackbar.LENGTH_LONG)
                .show()
        }
    }
}