package org.d3ifcool.catok.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewAnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.view.ActionMode
import org.d3ifcool.catok.R
import org.d3ifcool.catok.ui.beranda.produk.DataProdukAdapter


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