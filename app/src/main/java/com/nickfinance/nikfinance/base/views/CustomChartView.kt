package com.nickfinance.nikfinance.base.views

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.Toast
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.views.builders.TextState
import com.nickfinance.nikfinance.base.views.builders.ViewChartBuilder

class CustomChartView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val vChart: PieChart

    init {
        orientation = VERTICAL
        inflate(context, R.layout.view_custom_chart, this)
        vChart = findViewById(R.id.vChart)
    }

    fun initUi(builder: ViewChartBuilder) {
        with(builder.data) {
            val s = this.map { PieEntry(it.value.first.toFloat(), it.key) }
            val pieDataSet = PieDataSet(s, "")
            pieDataSet.colors = this.values.map { it.second }

            pieDataSet.valueTextSize = 0f
            pieDataSet.sliceSpace = 4f
            pieDataSet.isHighlightEnabled = true
            vChart.holeRadius = 95f
            vChart.legend.isEnabled = false
            vChart.setDrawEntryLabels(false)
            vChart.setHoleColor(context.resources.getColor(R.color.transparent, context.theme))
            vChart.data = PieData(pieDataSet)
            vChart.description.isEnabled = false
            vChart.animateXY(1000, 1000)
            vChart.invalidate();

            vChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    e?.let { builder.clickState?.invoke((e as PieEntry).label) }
                }

                override fun onNothingSelected() {

                }
            })
        }

        when (val textCenterState = builder.textCenterState) {
            is TextState.Hide -> {
                vChart.centerText = ""
            }

            is TextState.Show -> {
                vChart.centerText = SpannableStringBuilder().apply {
                    append(textCenterState.value)
                    append("\n")
                    append(textCenterState.spannable)
                }
                vChart.setCenterTextSize(20f)
                when (val colorState = textCenterState.colorState) {
                    is ColorState.FromResource -> {
                        vChart.setCenterTextColor(colorState.colorId)
                    }
                    is ColorState.FromIntColor -> {
                        vChart.setCenterTextColor(colorState.colorId)
                    }
                }
            }
        }
    }
}