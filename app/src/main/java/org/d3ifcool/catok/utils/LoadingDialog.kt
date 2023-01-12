package org.d3ifcool.catok.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import org.d3ifcool.catok.R

class LoadingDialog(private val activity: Activity) {
    private lateinit var progressDialog: AlertDialog
    fun start(){
        showDialog()
    }

    private fun showDialog(){
        val layout = R.layout.dialog_loading
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(layout,null)
        val builder = AlertDialog.Builder(activity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        progressDialog = builder.create()
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.show()
        progressDialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)
        progressDialog.window?.setGravity(Gravity.CENTER)
        val lp = progressDialog.window?.attributes
        lp?.dimAmount = 0.7f
        lp?.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        progressDialog.window?.attributes = lp

    }

    fun dismiss(){
        progressDialog.dismiss()
    }
}