package org.d3ifcool.catok.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.Entry
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.source.local.entities.FilterGrafikEntity
import org.d3ifcool.catok.core.data.source.local.entities.GrafikEntity
import java.io.ByteArrayOutputStream
import java.io.File
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
fun fromBitmapToString(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(),Base64.DEFAULT)
}
fun fromStringToBitmap(encodedString: String): Bitmap {
    val decodedByte = Base64.decode(encodedString.substring(encodedString.indexOf(","+1)), Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
}
//fun getEmptyEntry(): List<Entry>{
//    var dataGrafik: MutableList<GrafikEntity> = mutableListOf()
//    dataGrafik.add(GrafikEntity(0,0.0,""))
//    var result = ArrayList<Entry>()
//    var index = 1f
//    result.add(Entry(index,dataGrafik[0].totalTransaksi.toFloat()))
//    return result
//}
fun isFileExists(file: File): Boolean {
    return file.exists() && !file.isDirectory
}
fun <T> LiveData<T>.observeOnceAfterInit(owner: LifecycleOwner, observer: (T) -> Unit) {
    var firstObservation = true

    observe(owner, object: Observer<T>
    {
        override fun onChanged(value: T) {
            if(firstObservation)
            {
                firstObservation = false
            }
            else
            {
                removeObserver(this)
                observer(value)
            }
        }
    })
}


fun getListDateOfMonth(): List<FilterGrafikEntity> {
    val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
    val cal = Calendar.getInstance()
    val date = Date()
    cal.time = date
    cal[Calendar.DAY_OF_MONTH] = 1

    val myMonth = cal[Calendar.MONTH]
    val result = mutableListOf<FilterGrafikEntity>()

    while (myMonth == cal[Calendar.MONTH]) {
        result.add(FilterGrafikEntity(date = sdf.format(cal.time)))
        cal.add(Calendar.DAY_OF_MONTH, 1)
    }
    return result
}
fun getCurrentDate(): String {
    val currentDate = Calendar.getInstance().time
    val sdf = SimpleDateFormat("EEEE, d MMMM yyyy HH:mm", Locale("id", "ID"))
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
fun getCurrentMonthWithNumber(): String {
    val currentDate = Calendar.getInstance().time
    val sdf = SimpleDateFormat("WW MMMM", Locale("id", "ID"))
    return sdf.format(currentDate)
}
fun getCurrentDay(): String {
    val currentDate = Calendar.getInstance().time
    val sdf = SimpleDateFormat("DDDD", Locale("id", "ID"))
    return sdf.format(currentDate)
}

fun generateUuid(): String {
    val uuid = String.format("%040d", BigInteger(UUID.randomUUID().toString().replace("-", ""), 16))
    return uuid.substring(uuid.length - 5)
}
fun onTouch(v: View, motionEvent: MotionEvent): Boolean {
    val action = motionEvent.action
    if (action == MotionEvent.ACTION_DOWN) {
        v.animate().scaleXBy(95f).setDuration(5000).start()
        v.animate().scaleYBy(95f).setDuration(5000).start()
        return true
    } else if (action == MotionEvent.ACTION_UP) {
        v.animate().cancel()
        v.animate().scaleX(1f).setDuration(1000).start()
        v.animate().scaleY(1f).setDuration(1000).start()
        return true
    }
    return false
}

//fun EditText.addCurrencyFormatter() {
//    // Reference: https://stackoverflow.com/questions/5107901/better-way-to-format-currency-input-edittext/29993290#29993290
//
//    var setEditText = this.text.toString().trim()
//    lateinit var value: String
//    this.addTextChangedListener(object: TextWatcher {
//
//        private var current = ""
//
//        override fun afterTextChanged(s: Editable?) {
//        }
//
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        }
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//
//            if (s.toString() != setEditText) {
//                this@addCurrencyFormatter.removeTextChangedListener(this)
//                val replace = s.toString().replace("Rp. ".toRegex(), "")
//                val parsed = if (cleanString.isBlank()) 0.0 else cleanString.toDouble()
//
//                if(replace.isNotEmpty()){
//
//                }
//                // format the double into a currency format
//                val formated = NumberFormat.getCurrencyInstance(Locale("id","ID"))
//                    .format(parsed / 100)
//
//                current = formated
//                this@addCurrencyFormatter.setText(formated)
//                this@addCurrencyFormatter.setSelection(formated.length)
//                this@addCurrencyFormatter.addTextChangedListener(this)
//                Toast.makeText(context, cleanString, Toast.LENGTH_SHORT).show()
//
//            }
//        }
//    })
//
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


//
//}
 fun TextView.toSpannableString(s: String){
    val string = SpannableString(s)
    string.setSpan(UnderlineSpan(), 0,s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = string
}
@SuppressLint("ClickableViewAccessibility")
fun View.enableOnClickAnimation() {
    setOnTouchListener { v, motionEvent ->
        val action = motionEvent.action
        when(action){
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

@SuppressLint("ClickableViewAccessibility")
fun EditText.setupSearchView() {
    onFocusChangeListener = View.OnFocusChangeListener { v, bool ->
        if(v.hasFocus()) setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon_selector,0,R.drawable.ic_baseline_close_24,0)
        else {
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon_selector,0,0,0)
            v.clearFocus()
        }

    }

    setOnTouchListener(OnTouchListener { v, event ->
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var hasConsumed = false
        if (event.x >= width - totalPaddingRight) {
            if(event.action == MotionEvent.ACTION_UP){
                clearFocus()
                text.clear()
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon_selector,0,0,0)
                imm.hideSoftInputFromWindow(windowToken,0)
                return@OnTouchListener true
            }else setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon_selector,0,R.drawable.ic_baseline_close_24,0)

            hasConsumed = true
        }
        hasConsumed
    })
}
