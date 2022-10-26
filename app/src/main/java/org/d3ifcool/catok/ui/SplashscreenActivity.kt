package org.d3ifcool.catok.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.d3ifcool.catok.R

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
        },1500)
    }
}