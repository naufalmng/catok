package org.d3ifcool.catok.ui.beranda.transaksi

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.DataStorePreferences
import org.d3ifcool.catok.core.data.dataStore
import org.d3ifcool.catok.core.data.source.local.entities.GrafikEntity
import org.d3ifcool.catok.core.data.source.local.entities.HistoriTransaksiEntity
import org.d3ifcool.catok.databinding.FragmentTransaksiBinding
import org.d3ifcool.catok.ui.grafik.GrafikFragment
import org.d3ifcool.catok.ui.grafik.GrafikViewModel
import org.d3ifcool.catok.ui.main.SharedViewModel
import org.d3ifcool.catok.utils.enableOnClickAnimation
import org.d3ifcool.catok.utils.getCurrentDate2
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception

class TransaksiFragment : Fragment() {

    private var _binding: FragmentTransaksiBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransaksiViewModel by viewModel()
    private val grafikViewModel: GrafikViewModel by viewModel()
    private lateinit var historiTransaksiAdapter: HistoriTransaksiAdapter
    private lateinit var dataPembayaranAdapter: DataPembayaranAdapter
    lateinit var toolbar: Toolbar

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val dataStorePreferences: DataStorePreferences by lazy {
        DataStorePreferences(requireActivity().dataStore)
    }

    private var totalTransaksi = 0.0 // 6000
    private var tanggal = ""

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val handler = object : HistoriTransaksiAdapter.ClickHandler{
        override fun onClick(transaksi: HistoriTransaksiEntity) {
            findNavController().navigate(TransaksiFragmentDirections.actionTransaksiFragmentToDetailHistoriTransaksiDialog(transaksi))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        sharedViewModel.isBackPressed.value = -1
        binding.llHeader.title.text = getString(R.string.histori_transaksi)
        toolbar = inflater.inflate(R.layout.toolbar, container, false) as Toolbar
        toolbar.title = getString(R.string.data_produk)
        toolbar.collapseActionView()
        return binding.root
    }


    override fun onResume() {
        super.onResume()
//        Handler(Looper.getMainLooper()).postDelayed({
//            viewModel.isNavReady.value = true
//        },1000)
        sharedViewModel.isBackPressed.value = -1
    }

    private fun setupListeners() {
        with(binding) {
//            setupSearchView()
            viewModel.isNavReady.observe(viewLifecycleOwner){
                btnTambah.isClickable = it
            }
            btnTambah.enableOnClickAnimation()
            btnTambah.setOnClickListener {
                viewModel.getDataProduk.observe(viewLifecycleOwner) {
                    if (it.size>0) {
                        historiTransaksiAdapter.resetSelection()
                        if(findNavController().currentDestination?.id==R.id.transaksiFragment){
                            findNavController().navigate(R.id.action_transaksiFragment_to_transaksiDialog)
                        }
                    }else Toast.makeText(requireContext(), "Data produk masih kosong !", Toast.LENGTH_SHORT).show()
                }
            }
            llHeader.btnSwitchLayout.setOnClickListener {
                if(TransaksiAdapter.GridViewHolder.produkIdList.size>0 && TransaksiAdapter.LinearViewHolder.produkIdList.size<1){
                    TransaksiAdapter.GridViewHolder.produkIdList.clear()
                }
                viewModel.isLinearLayoutManager = !viewModel.isLinearLayoutManager

                lifecycleScope.launch {
                    dataStorePreferences.saveLayoutSetting(
                        requireContext(), viewModel.isLinearLayoutManager
                    )
                }
                setupObservers()
                setupLayoutSwitcher()
                setupLayoutSwitcherIcon()

            }
            swipeRefreshLayout.setOnRefreshListener {
                setupObservers()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        historiTransaksiAdapter = HistoriTransaksiAdapter(viewModel.isLinearLayoutManager, handler)
        setupMenu()
        setupListeners()
        setupObservers()
        setupLayoutPreference()
        sharedViewModel.isBackPressed.value = -1
    }

    private fun setupLayoutPreference() {
        dataStorePreferences.layoutPrefFlow.asLiveData()
            .observe(viewLifecycleOwner) {
                viewModel.isLinearLayoutManager = it
                setupLayoutSwitcher()
                setupLayoutSwitcherIcon()
                setupObservers()
            }
    }

    private fun setupObservers() {
        viewModel.isDataTransaksiEmpty.observe(viewLifecycleOwner) {
            if (it == false) {
                binding.dataKosong.visibility = View.GONE
                binding.recyclerViewHistoriTransaksi.visibility = View.VISIBLE
            } else {
                binding.dataKosong.visibility = View.VISIBLE
                binding.recyclerViewHistoriTransaksi.visibility = View.GONE
            }
        }
        viewModel.getDataHistoriTransaksi.observe(viewLifecycleOwner) {
//            transaksiViewModel.getDataHistoriTransaksi.observe(viewLifecycleOwner){
//                Log.d(GrafikFragment.TAG, "dataHistoriTransaksi: $it")
//                Log.d("GrafikFragment", "totalTransaksi: ${ viewModel.totalTransaksi.value.toString()}")
//
//                if(!it.isNullOrEmpty()){
//                    result = 0.0
//                    it.forEach { data ->
//                        result += data.total
//                        Log.d("GrafikFragment", "totalTransaksi: ${result}")
//                    }
//                    viewModel.totalTransaksi.value = viewModel.totalTransaksi.value!!+result
//                }
//            }
            if (it != null) {
                viewModel.isDataTransaksiEmpty.value = it.size<1
                setuprecyclerViewHistoriTransaksis()
                historiTransaksiAdapter.updateData(it)
                setupLayoutSwitcher()
                binding.swipeRefreshLayout.isRefreshing = false
//                totalTransaksi = 0.0
//                sharedViewModel.totalTransaksi.value = 0.0
//                try {
//                    it.forEach{data->
//                        totalTransaksi+=data.total
//                    }
//                }catch (e: Exception){}
//                finally {
//                    sharedViewModel.totalTransaksi.value = sharedViewModel.totalTransaksi.value!!+totalTransaksi
//                }
                Log.d("TransaksiFragment", "Total Transaksi: $totalTransaksi")
            }
        }
//        transaksiViewModel.getDataHistoriTransaksi.observe(viewLifecycleOwner){
//            Log.d(GrafikFragment.TAG, "dataHistoriTransaksi: $it")
//            Log.d("GrafikFragment", "totalTransaksi: ${ viewModel.totalTransaksi.value.toString()}")
//
//            if(!it.isNullOrEmpty()){
//                result = 0.0
//                it.forEach { data ->
//                    result += data.total
//                    Log.d("GrafikFragment", "totalTransaksi: ${result}")
//                }
//                viewModel.totalTransaksi.value = viewModel.totalTransaksi.value!!+result
//            }
//        }
//        viewModel.getDataGrafik.observe(viewLifecycleOwner){
//            if(it.isNullOrEmpty()){
//                if (totalTransaksi!=0.0) viewModel.insertDataGrafik(GrafikEntity(totalTransaksi = totalTransaksi, tanggal = tanggal))
//            }
//            else{
//                if(getCurrentDate2() != tanggal) viewModel.insertDataGrafik(GrafikEntity(totalTransaksi = totalTransaksi, tanggal = tanggal))
//                else viewModel.updateDataGrafik(totalTransaksi,tanggal)
//            }
//        }
//        if(totalTransaksi==0.0){
//            Log.d("TransaksiFragment", "setupObservers: masuk if")
//            viewModel.insertDataGrafik(GrafikEntity(totalTransaksi = totalTransaksi, tanggal = tanggal))
//        }else{
//            Log.d("TransaksiFragment", "setupObservers: masuk else")
//            viewModel.getDataGrafik.observe(viewLifecycleOwner){
//                for (i in it.indices){
//                    if(it[i].tanggal!= tanggal) viewModel.insertDataGrafik(GrafikEntity(totalTransaksi = totalTransaksi, tanggal = tanggal))
//                    else viewModel.updateDataGrafik(totalTransaksi,tanggal)
//                }
//            }
//        }
    }

    private fun setuprecyclerViewHistoriTransaksis() {
        historiTransaksiAdapter = HistoriTransaksiAdapter(viewModel.isLinearLayoutManager, handler)
        with(binding.recyclerViewHistoriTransaksi) {
            isNestedScrollingEnabled = true
            adapter = historiTransaksiAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupLayoutSwitcherIcon() {
        if (viewModel.isLinearLayoutManager) binding.llHeader.btnSwitchLayout.setBackgroundResource(R.drawable.ic_linear_layout)
        else binding.llHeader.btnSwitchLayout.setBackgroundResource(R.drawable.ic_grid_layout)
    }

    private fun setupLayoutSwitcher() {
        if (viewModel.isLinearLayoutManager) binding.recyclerViewHistoriTransaksi.layoutManager = LinearLayoutManager(this.requireContext())
        else binding.recyclerViewHistoriTransaksi.layoutManager = GridLayoutManager(this.requireContext(), 2)
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {

            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem?.actionView as SearchView

                searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
                    .setBackgroundColor(Color.TRANSPARENT)
                searchView.queryHint = getString(R.string.pencarian)
                searchView.imeOptions = EditorInfo.IME_ACTION_DONE
                toolbar.addView(searchView)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(text: String?): Boolean {

                        return false
                    }

                    override fun onQueryTextChange(text: String?): Boolean {
                        historiTransaksiAdapter.filter.filter(text.toString())
                        if (text?.isNotEmpty() == true) {
                            binding.dataKosong.visibility = View.VISIBLE
                            if (historiTransaksiAdapter.historiHistoriTransaksiFilterList.size <= 0) {
                                binding.dataKosong.visibility = View.VISIBLE
                            } else {
                                binding.dataKosong.visibility = View.GONE
                            }
                        } else {
                            if(historiTransaksiAdapter.historiTransaksiList.isNullOrEmpty()) binding.dataKosong.visibility = View.VISIBLE
                            else {
                                historiTransaksiAdapter.historiHistoriTransaksiFilterList = historiTransaksiAdapter.historiTransaksiList
                                binding.dataKosong.visibility = View.GONE
                            }
                        }

                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    android.R.id.home -> {
                        findNavController().navigate(R.id.action_transaksiFragment_to_berandaFragment)
                        return true
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}