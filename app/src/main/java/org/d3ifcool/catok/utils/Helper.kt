package org.d3ifcool.catok.utils

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.MotionEvent
import android.view.View
import java.math.BigInteger
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


fun String.addUnderline(): SpannableString {
    val spannableString = SpannableString(this)
    spannableString.setSpan(UnderlineSpan(),0,spannableString.length,0)
    return spannableString
}
fun getCurrentDate(): String {
    val currentDate = Calendar.getInstance().time
    val sdf = SimpleDateFormat("EEEE, d MMMM yyyy HH:mm", Locale("id", "ID"))
    return sdf.format(currentDate)
}

fun getCurrentMonthAndYear(): String {
    val currentDate = System.currentTimeMillis()
    val sdf = SimpleDateFormat("MMMM yyyy", Locale("id", "ID"))
    return sdf.format(currentDate)
}

fun getCurrentDate2(): String {
    val currentDate = Calendar.getInstance().time
    val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
    return sdf.format(currentDate)
}

fun getCurrentMonth(): String {
    val currentDate = Calendar.getInstance().time
    val sdf = SimpleDateFormat("MMMM", Locale("id", "ID"))
    return sdf.format(currentDate)
}


fun generateUuid(): String {
    val uuid = String.format("%040d", BigInteger(UUID.randomUUID().toString().replace("-", ""), 16))
    return uuid.substring(uuid.length - 5)
}
    fun Double.toRupiahFormat(): String{
        val localeId = Locale("IND","ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeId)
        val formatRupiah = numberFormat.format(this)
        val split = formatRupiah.split(",")
        val length = split[0].length
        return split[0].substring(0,2)+". "+split[0].substring(2,length)
    }

fun Double.toRupiahFormatV2(): String {
    val formatter = NumberFormat.getInstance(Locale("id", "ID"))
    val decimalFormatSymbols: DecimalFormatSymbols =
        (formatter as DecimalFormat).decimalFormatSymbols
    decimalFormatSymbols.currencySymbol = ""
    val rupiahFormat = formatter.format(this)
    formatter.decimalFormatSymbols = decimalFormatSymbols

    return rupiahFormat.replace(",", ".")
}



@SuppressLint("ClickableViewAccessibility")
fun View.enableOnClickAnimation() {
    setOnTouchListener { v, motionEvent ->
        when(motionEvent.action){
            MotionEvent.ACTION_DOWN -> {
                v.animate().scaleX(0.95f).setDuration(1).start()
                v.animate().scaleY(0.95f).setDuration(1).start()
                return@setOnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                v.animate().cancel()
                v.animate().scaleX(1.0f).setDuration(1).start()
                v.animate().scaleY(1.0f).setDuration(1).start()
                return@setOnTouchListener false
            }
            MotionEvent.ACTION_CANCEL -> {
                v.animate().cancel()
                v.animate().scaleX(1.0f).setDuration(1).start()
                v.animate().scaleY(1.0f).setDuration(1).start()
                return@setOnTouchListener false
            }
        }
        return@setOnTouchListener false
    }
}
