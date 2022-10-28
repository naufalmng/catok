package org.d3ifcool.catok.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import org.d3ifcool.catok.R
import org.d3ifcool.catok.ui.MainActivity

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
                return@setOnTouchListener true
            }
            MotionEvent.ACTION_UP -> {
                v.animate().cancel()
                v.animate().scaleX(1.0f).setDuration(1).start()
                v.animate().scaleY(1.0f).setDuration(1).start()
                return@setOnTouchListener true
            }
            MotionEvent.ACTION_CANCEL -> {
                v.animate().cancel()
                v.animate().scaleX(1.0f).setDuration(1).start()
                v.animate().scaleY(1.0f).setDuration(1).start()
                return@setOnTouchListener true
            }
        }
        return@setOnTouchListener false
    }
}