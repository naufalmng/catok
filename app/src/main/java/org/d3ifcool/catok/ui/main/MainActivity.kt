package org.d3ifcool.catok.ui.main

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupWithNavController
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.source.local.entities.EditQuantityDialog
import org.d3ifcool.catok.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var config: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }
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
                    binding.toolbar.toolbar.visibility = View.GONE
                }
                R.id.pengaturanFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.toolbar.visibility = View.GONE
                }
                else -> {
                    binding.bottomNav.visibility = View.GONE
                    binding.toolbar.toolbar.visibility = View.VISIBLE
                }
            }
        }
        config = AppBarConfiguration(navController.graph)
        binding.toolbar.toolbar.setupWithNavController(navController,config)


        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(com.google.android.material.R.anim.abc_fade_in)
            .setExitAnim(com.google.android.material.R.anim.abc_fade_out)
            .setPopEnterAnim(com.google.android.material.R.anim.abc_popup_enter)
            .setPopExitAnim(com.google.android.material.R.anim.abc_popup_exit)
            .setPopUpTo(navController.graph.startDestinationRoute,false)
            .build()

        binding.toolbar.toolbar.setNavigationOnClickListener {
            navigateUp(navController,config)
        }
        setSupportActionBar(binding.toolbar.toolbar)
//        binding.toolbar.toolbar.inflateMenu(R.menu.search_menu)

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
        var navController :NavController= Navigation.findNavController(this, R.id.fragmentContainerView)
        Log.d("MainActivity", "onBackPressed: isBackPressed = ${sharedViewModel.isBackPressed.value}")
        if(navController.currentDestination?.id == R.id.dataProdukFragment){
            sharedViewModel.isBackPressed.value = -1
        }
        if(navController.currentDestination?.id != R.id.berandaFragment){
            if(sharedViewModel.isBackPressed.value==-1) navigateUp(navController,config)
            Log.d("MainActivity", "onBackPressed: masuk")
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                sharedViewModel._isDialogPembayaran.value = destination.label == "dialog_transaksi"
            }
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