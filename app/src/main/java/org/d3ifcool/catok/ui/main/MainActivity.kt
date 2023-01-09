package org.d3ifcool.catok.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.DataStorePreferences
import org.d3ifcool.catok.core.data.dataStore
import org.d3ifcool.catok.core.data.source.local.entities.ProfilEntity
import org.d3ifcool.catok.databinding.ActivityMainBinding
import org.d3ifcool.catok.ui.beranda.transaksi.TransaksiAdapter
import org.d3ifcool.catok.ui.beranda.transaksi.TransaksiViewModel
import org.d3ifcool.catok.ui.grafik.GrafikViewModel
import org.d3ifcool.catok.ui.pengaturan.PengaturanViewModel
import org.d3ifcool.catok.utils.getCurrentDate2
import org.d3ifcool.catok.utils.getCurrentMonth
import org.d3ifcool.catok.utils.getListDateOfMonth
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var config: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }
    private var isBackButtonPressedOnce = false
    private val viewModel: TransaksiViewModel by viewModel()
    private val grafikViewModel: GrafikViewModel by viewModel()
    private val pengaturanViewModel: PengaturanViewModel by viewModel()
    private val dataStorePreferences: DataStorePreferences by lazy {
        DataStorePreferences(this.dataStore)
    }
//    private val dataProdukViewModel: DataProdukViewModel by viewModel()

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "configuration change", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        viewModel.clearGraphicData()
//        viewModel.deleteHistoriTransaksi()

        viewModel.getDataHistoriTransaksi.observe(this){
            for (i in it){
//                i.produkNameList produk.replace("\n",".").replace(" ","")
//                Log.d("MainActivity", "onCreate: ${i.produk}")
//                Log.d("MainActivity", "onCreate: betul? ${i.produk.contains("\n")}")

            }
        }
       if (pengaturanViewModel.getDataProfil.value==null){
            pengaturanViewModel.insertProfil(ProfilEntity(id = 1, namaToko = getString(R.string.nama_toko), gambar = ContextCompat.getDrawable(this,R.drawable.ic_catok)!!.toBitmap()))
        }
        dataStorePreferences.currentDatePrefFlow.asLiveData()
            .observe(this){ date->
                if(date==null){
                    lifecycleScope.launch {
                        dataStorePreferences.saveCurrentDate(this@MainActivity, getCurrentDate2())
                    }
//                    viewModel.clearFilterGrafik()
                    for (i in getListDateOfMonth().indices){
                        viewModel.insertFilterGrafik(getListDateOfMonth()[i])
                    }
                }else{
                    if(date != getCurrentDate2()) {
                        sharedViewModel.totalTransaksi.value = 0.0
                        lifecycleScope.launch {
                            dataStorePreferences.saveCurrentDate(this@MainActivity, getCurrentDate2())
                        }
//                        for (i in getListDateOfMonth().indices){
//                            viewModel.insertFilterGrafik(getListDateOfMonth()[i])
//                        }
                    }
                }


            }
        dataStorePreferences.currentMonthPrefFlow.asLiveData()
            .observe(this){month->
                if(month==null){
                    lifecycleScope.launch {
                        dataStorePreferences.saveCurrentMonth(this@MainActivity, getCurrentMonth())
                    }
                    for (i in getListDateOfMonth().indices){
                        viewModel.insertFilterGrafik(getListDateOfMonth()[i])
                    }
                } else{
                    if(month != getCurrentMonth()) {
                        lifecycleScope.launch {
                            dataStorePreferences.saveCurrentMonth(this@MainActivity, getCurrentMonth())
                        }
                        for (i in getListDateOfMonth().indices){
                            viewModel.insertFilterGrafik(getListDateOfMonth()[i])
                        }
                    }
                }
            }
//        viewModel.deleteTransaksi()
//        viewModel.deleteTransaksiProduk()
//        viewModel.deleteGrafik()
//        viewModel.deleteHistoriTransaksi()
        Log.d("Transaksi", "onCreate: ${viewModel.getDataHistoriTransaksi.value}")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener{_,destination,_->
            when(destination.id){
                R.id.berandaFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
                R.id.detailHistoriTransaksiFragment-> {
                    binding.toolbar.toolbar.visibility = View.GONE
                }
                R.id.transaksiFragment ->{
                    binding.bottomNav.visibility = View.GONE
                    binding.toolbar.toolbar.visibility = View.VISIBLE
                    TransaksiAdapter.LinearViewHolder.produkIdList.clear()
                    TransaksiAdapter.LinearViewHolder.produkNameList.clear()
                    TransaksiAdapter.LinearViewHolder.produkQtyList.clear()
                    TransaksiAdapter.LinearViewHolder.produkPriceList.clear()
                    TransaksiAdapter.LinearViewHolder.tempProduk.clear()
                    TransaksiAdapter.LinearViewHolder.tempCounter.clear()
                    TransaksiAdapter.GridViewHolder.produkIdList.clear()
                    TransaksiAdapter.GridViewHolder.produkNameList.clear()
                    TransaksiAdapter.GridViewHolder.produkQtyList.clear()
                    TransaksiAdapter.GridViewHolder.produkPriceList.clear()
                    sharedViewModel._tempDataPembayaran.value?.clear()
                    viewModel.tempDataPembayaran.clear()
                    viewModel.dataPembayaran.value?.clear()
                    sharedViewModel.tempDataProduk.value = null
                }
                R.id.grafikFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.toolbar.visibility = View.GONE
                }
                R.id.pengaturanFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.toolbar.visibility = View.GONE
                }
                R.id.editNamaDialog -> {
                    binding.bottomNav.visibility = View.VISIBLE
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
        binding.bottomNav.setupWithNavController(navController)
        setSupportActionBar(binding.toolbar.toolbar)
//        binding.toolbar.toolbar.inflateMenu(R.menu.search_menu)

//        with(binding.bottomNav){
//            setOnItemSelectedListener{ item->
//                when(item.itemId){
//                    R.id.berandaFragment ->{
//                        navController.navigate(R.id.berandaFragment,null,options)
//                    }
//                    R.id.grafikFragment ->{
//                        navController.navigate(R.id.grafikFragment,null,options)
//                    }
//                    R.id.pengaturanFragment ->{
//                        navController.navigate(R.id.pengaturanFragment,null,options)
//                    }
//                }
//            true
//            }
////            setOnItemReselectedListener{ item->
////                when(item.itemId){
////                    R.id.berandaFragment ->{
////                        navController.navigate(R.id.berandaFragment,null,options)
////                    }
////                    R.id.grafikFragment ->{
////                        navController.navigate(R.id.grafikFragment,null,options)
////                    }
////                    R.id.pengaturanFragment ->{
////                        navController.navigate(R.id.pengaturanFragment,null,options)
////                    }
////                }
////
////            }
//        }
//        requestPermission()
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),PackageManager.PERMISSION_GRANTED)
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

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.namaToko.value = null
        sharedViewModel.fotoToko.value = null
    }


}