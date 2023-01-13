package org.d3ifcool.catok.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import org.d3ifcool.catok.R
import org.d3ifcool.catok.ui.main.MainActivity
import org.d3ifcool.catok.ui.onboarding.OnBoardingActivity

@SuppressLint("CustomSplashScreen")
class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
            startActivity(Intent(this, OnBoardingActivity::class.java))
        },750)
    }
}