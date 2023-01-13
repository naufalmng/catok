package org.d3ifcool.catok.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3ifcool.catok.R
import org.d3ifcool.catok.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
    }
}