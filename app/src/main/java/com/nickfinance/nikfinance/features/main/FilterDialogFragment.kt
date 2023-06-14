package com.nickfinance.nikfinance.features.main

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.views.CustomSingleLineView
import com.nickfinance.nikfinance.base.views.builders.*
import com.nickfinance.nikfinance.domain.models.FilterData
import com.nickfinance.nikfinance.utils.DateFormats
import com.nickfinance.nikfinance.utils.extensions.parcelable
import java.util.*

class FilterDialogFragment : DialogFragment() {

    private lateinit var ivClose: ImageView
    private lateinit var tvSave: TextView
    private lateinit var etMinAmount: EditText
    private lateinit var etMaxAmount: EditText
    private lateinit var cMinCreatedAt: CustomSingleLineView
    private lateinit var cMaxCreatedAt: CustomSingleLineView
    private lateinit var cbArchive: MaterialCheckBox
    private lateinit var data: FilterData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_filter, container, false)
        ivClose = view.findViewById(R.id.ivClose)
        tvSave = view.findViewById(R.id.tvSave)
        etMinAmount = view.findViewById(R.id.etMinAmount)
        etMaxAmount = view.findViewById(R.id.etMaxAmount)
        cMinCreatedAt = view.findViewById(R.id.cMinCreatedAt)
        cMaxCreatedAt = view.findViewById(R.id.cMaxCreatedAt)
        cbArchive = view.findViewById(R.id.cbArchive)
        isCancelable = false
        return view
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        data = requireArguments().parcelable(Companion.data)!!
        initUi()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initUi() {
        etMinAmount.setText((data.minAmount ?: "").toString())
        etMaxAmount.setText((data.maxAmount ?: "").toString())
        cbArchive.isChecked = data.isArchived
        cbArchive.setOnCheckedChangeListener { _, isChecked ->
            data = data.copy(isArchived = isChecked)
        }
        cMinCreatedAt.initUI(
            ViewSingleLineBuilder.newBuilder()
                .setStateTextLeft(
                    TextState.Show(
                        value = "Период от"
                    )
                )
                .setStateTextRight(
                    TextState.Show(
                        value = if (data.minCreatedAt == null) {
                            "Не указано"
                        } else {
                            DateFormats.getString(
                                Date(data.minCreatedAt!!),
                                DateFormats.Companion.Format.DD_MM_YYYY_HH_MM
                            )
                        }
                    )
                )
                .setStateIconRight(
                    if (data.minCreatedAt == null) {
                        IconState.Hide
                    } else {
                        IconState.ShowFromRes(
                            iconId = R.drawable.ic_close,
                            clickState = ClickState.Action {
                                data = data.copy(minCreatedAt = null)
                                initUi()
                            }
                        )
                    }
                )
                .setMarginState(
                    MarginState.Custom(
                        top = 16,
                        bottom = 0,
                        left = 16,
                        right = 16
                    )
                )
                .setStateRoot(
                    RootState.Fill(
                        clickState = ClickState.Action {
                            setTime(TypePeriod.FROM)
                        }
                    )
                )
                .build()
        )
        cMaxCreatedAt.initUI(
            ViewSingleLineBuilder.newBuilder()
                .setStateTextLeft(
                    TextState.Show(
                        value = "Период до"
                    )
                )
                .setStateTextRight(
                    TextState.Show(
                        value = if (data.maxCreatedAt == null) {
                            "Не указано"
                        } else {
                            DateFormats.getString(
                                Date(data.maxCreatedAt!!),
                                DateFormats.Companion.Format.DD_MM_YYYY_HH_MM
                            )
                        }
                    )
                )
                .setStateIconRight(
                    if (data.maxCreatedAt == null) {
                        IconState.Hide
                    } else {
                        IconState.ShowFromRes(
                            iconId = R.drawable.ic_close,
                            clickState = ClickState.Action {
                                data = data.copy(maxCreatedAt = null)
                                initUi()
                            }
                        )
                    }
                )
                .setMarginState(
                    MarginState.Custom(
                        top = 16,
                        bottom = 0,
                        left = 16,
                        right = 16
                    )
                )
                .setStateRoot(
                    RootState.Fill(
                        clickState = ClickState.Action {
                            setTime(TypePeriod.TO)
                        }
                    )
                )
                .build()
        )
        ivClose.setOnClickListener {
            dismiss()
        }
        tvSave.setOnClickListener {
            setFragmentResult(
                getString(R.string.key_filter),
                bundleOf(getString(R.string.data_filter) to data)
            )
            dismiss()
        }
        etMinAmount.doOnTextChanged { text, _, _, _ ->
            data = data.copy(
                minAmount = if (text.isNullOrBlank()) null else text.toString().toDouble()
            )
        }
        etMaxAmount.doOnTextChanged { text, _, _, _ ->
            data = data.copy(
                maxAmount = if (text.isNullOrBlank()) null else text.toString().toDouble()
            )
        }
    }

    private fun setTime(tp: TypePeriod) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setNegativeButtonText("Отмена")
            .setPositiveButtonText("Ок")
            .setTitleText("Выберите дату")
            .build()

        datePicker.show(this.childFragmentManager, "DatePicker")

        datePicker.addOnPositiveButtonClickListener { dateTime ->

            val cal = Calendar.getInstance()
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(cal.get(Calendar.HOUR_OF_DAY))
                .setMinute(cal.get(Calendar.MINUTE))
                .setNegativeButtonText("Отмена")
                .setPositiveButtonText("Ок")
                .build()

            timePicker.show(this.childFragmentManager, "TimePicker")

            timePicker.addOnPositiveButtonClickListener {
                val millies =
                    (timePicker.hour * 3_600_000L + timePicker.minute * 60_000L) - TimeZone.getDefault().rawOffset
                data = if (tp == TypePeriod.FROM) {
                    data.copy(minCreatedAt = dateTime + millies)
                } else {
                    data.copy(maxCreatedAt = dateTime + millies)
                }
                initUi()
            }

        }
    }

    enum class TypePeriod {
        FROM, TO
    }

    companion object {

        private const val data = "DATA"

        fun openDialog(
            parent: Fragment, filterData: FilterData
        ): FilterDialogFragment {
            val dialog = FilterDialogFragment()
            dialog.arguments = bundleOf(data to filterData)
            dialog.show(parent.parentFragmentManager, null)
            return dialog
        }
    }
}