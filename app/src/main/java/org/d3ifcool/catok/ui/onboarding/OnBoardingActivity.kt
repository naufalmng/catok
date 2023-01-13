package org.d3ifcool.catok.ui.onboarding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3ifcool.catok.R
import org.d3ifcool.catok.databinding.ActivityOnBoardingBinding
import org.d3ifcool.catok.ui.main.MainActivity
import java.lang.Boolean

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences(getString(R.string.app_name),Context.MODE_PRIVATE)
        if(!sharedPreferences.getBoolean("isOnBoardingOpened", false)){
            val editor = sharedPreferences.edit()
            editor.putBoolean("isOnBoardingOpened", Boolean.TRUE)
            editor.apply()
        }else{
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
        binding.btnSelanjutnya.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }
}