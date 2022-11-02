package org.d3ifcool.catok.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import org.d3ifcool.catok.R
import org.d3ifcool.catok.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener{_,destination,_->
            if(destination.id != R.id.berandaFragment) binding.bottomNav.visibility = View.GONE
            else binding.bottomNav.visibility = View.VISIBLE
        }
        val config = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController,config)


        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(com.google.android.material.R.anim.abc_fade_in)
            .setExitAnim(com.google.android.material.R.anim.abc_fade_out)
            .setPopEnterAnim(com.google.android.material.R.anim.abc_popup_enter)
            .setPopExitAnim(com.google.android.material.R.anim.abc_popup_exit)
            .setPopUpTo(navController.graph.startDestinationRoute,false)
            .build()

        with(binding.bottomNav){
            setOnItemSelectedListener{ item->
                when(item.itemId){
                    R.id.berandaFragment ->{
                        navController.navigate(R.id.berandaFragment,null,options)
                    }
                    R.id.profilFragment ->{
                        navController.navigate(R.id.profilFragment,null,options)
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
                    R.id.profilFragment ->{
                        navController.navigate(R.id.profilFragment,null,options)
                    }
                    R.id.pengaturanFragment ->{
                        navController.navigate(R.id.pengaturanFragment,null,options)
                    }
                }

            }
        }
    }

}