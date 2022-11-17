package org.d3ifcool.catok.ui

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import org.d3ifcool.catok.R
import org.d3ifcool.catok.databinding.ActivityMainBinding
import org.d3ifcool.catok.ui.beranda.produk.DataProdukViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var config: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var isBackButtonPressedOnce = false
//    private val dataProdukViewModel: DataProdukViewModel by viewModel()

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "configuration change", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener{_,destination,_->
            when(destination.id){
                R.id.berandaFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
                R.id.grafikFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.GONE
                }
                R.id.pengaturanFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.GONE
                }
                else -> {
                    binding.bottomNav.visibility = View.GONE
                    binding.toolbar.visibility = View.VISIBLE
                }
            }
        }
        config = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController,config)


        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(com.google.android.material.R.anim.abc_fade_in)
            .setExitAnim(com.google.android.material.R.anim.abc_fade_out)
            .setPopEnterAnim(com.google.android.material.R.anim.abc_popup_enter)
            .setPopExitAnim(com.google.android.material.R.anim.abc_popup_exit)
            .setPopUpTo(navController.graph.startDestinationRoute,false)
            .build()

        binding.toolbar.setNavigationOnClickListener {
            navigateUp(navController,config)
        }

        with(binding.bottomNav){
            setOnItemSelectedListener{ item->
                when(item.itemId){
                    R.id.berandaFragment ->{
                        navController.navigate(R.id.berandaFragment,null,options)
                    }
                    R.id.grafikFragment ->{
                        navController.navigate(R.id.grafikFragment,null,options)
                    }
                    R.id.pengaturanFragment ->{
                        navController.navigate(R.id.pengaturanFragment,null,options)
                    }
                }
            true
            }
            setOnItemReselectedListener{ item->
                when(item.itemId){
                    R.id.berandaFragment ->{
                        navController.navigate(R.id.berandaFragment,null,options)
                    }
                    R.id.grafikFragment ->{
                        navController.navigate(R.id.grafikFragment,null,options)
                    }
                    R.id.pengaturanFragment ->{
                        navController.navigate(R.id.pengaturanFragment,null,options)
                    }
                }

            }
        }

    }

    override fun onBackPressed() {
        if(navController.currentDestination?.id != R.id.berandaFragment){
            navigateUp(navController,config)
            return
        }
        if(navController.graph.startDestinationId == navController.currentDestination?.id && isBackButtonPressedOnce )  {
            finish()
            return
        }
        this.isBackButtonPressedOnce = true
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            isBackButtonPressedOnce = false
        },1000)
    }


}