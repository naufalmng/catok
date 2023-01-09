package org.d3ifcool.catok.utils

import com.github.mikephil.charting.components.AxisBase

import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*


class DateValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return getCurrentDate2().toString()
    }
}