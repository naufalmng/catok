@file:Suppress("PrivatePropertyName", "NestedLambdaShadowedImplicitParameter")

package org.d3ifcool.catok.ui.beranda.transaksi

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.DataStorePreferences
import org.d3ifcool.catok.core.data.dataStore
import org.d3ifcool.catok.core.data.source.local.entities.GrafikEntity
import org.d3ifcool.catok.core.data.source.local.entities.HistoriTransaksiEntity
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.core.data.source.local.entities.TransaksiEntity
import org.d3ifcool.catok.core.data.source.local.model.DataPembayaran
import org.d3ifcool.catok.core.data.source.local.model.Produk
import org.d3ifcool.catok.databinding.DialogTransaksiBinding
import org.d3ifcool.catok.ui.main.SharedViewModel
import org.d3ifcool.catok.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList


class TransaksiDialog : Fragment() {

    private var currentDate: String = ""
    private lateinit var searchItem: MenuItem

    private val TAG = this.javaClass.name
    private var _binding: DialogTransaksiBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransaksiViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }
    private lateinit var searchView: SearchView
    private lateinit var transaksiAdapter: TransaksiAdapter
    lateinit var toolbar: Toolbar

    private val dataStorePreferences: DataStorePreferences by lazy {
        DataStorePreferences(requireActivity().dataStore)
    }

    fun setBtnSelanjutnyaTint(isOrange: Boolean){
        if(isOrange) binding.btnSelanjutnya.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.orange)
        else binding.btnSelanjutnya.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.abuB)

    }

    private val handler = object : TransaksiAdapter.ClickHandler{

        override fun onPlusButtonClick(position: Int, produk: ArrayList<ProdukEntity>, counter: Int, produkData: Produk) {
            sharedViewModel.tempDataProduk.value = produkData
            val dataPembayaran : MutableList<DataPembayaran> = mutableListOf()
            for(i in produkData.produkIdList!!.indices){
                dataPembayaran.add(i,DataPembayaran(produkData.produkIdList!![i]!!, produkData.produkNameList!![i]!!, produkData.produkQtyList!![i]!!, produkData.produkSatuanPerList!![i]!!, produkData.hargaProdukList!![i]!!))
            }
            sharedViewModel._tempDataPembayaran.value = dataPembayaran
            viewModel.tempDataPembayaran = dataPembayaran
            Log.d(TAG, "onPlusButtonClick: ${sharedViewModel.tempDataProduk.value}")
            binding.llHeader.resetSwitch.isChecked = true
            binding.llHeader.resetSwitch.isEnabled = true
            if(counter==1){
                setBtnSelanjutnyaTint(true)
            }
            if (counter > 1) {
                setBtnSelanjutnyaTint(true)
            }
        }


        override fun onMinButtonClick(position: Int, produk: ArrayList<ProdukEntity>,counter: Int,produkData: Produk) {
            val dataPembayaran : MutableList<DataPembayaran> = mutableListOf()

            sharedViewModel.tempDataProduk.value = produkData
            Log.d(TAG, "onMinButtonClick: ${sharedViewModel.tempDataProduk.value}")

            if(counter==0){
                for(i in produkData.produkIdList!!.indices){
                    dataPembayaran.add(i,DataPembayaran(produkData.produkIdList!![i]!!, produkData.produkNameList!![i]!!, produkData.produkQtyList!![i]!!,produkData.produkSatuanPerList!![i]!!, produkData.hargaProdukList!![i]!!))
                }
                sharedViewModel._tempDataPembayaran.value = dataPembayaran
                viewModel.tempDataPembayaran = dataPembayaran
                Log.d(TAG, "onMinButtonClick: $produkData")

                Log.d(TAG, "onMinButtonClick: ${viewModel.tempDataPembayaran}")
                Log.d(TAG, "onMinButtonClick: ${sharedViewModel.tempDataProduk.value}")


                sharedViewModel._tempDataPembayaran.observe(viewLifecycleOwner){
                    Log.d(TAG, "onMinButtonClick: tempDataPembayaran = ${it.size}")
                    Log.d(TAG, "onMinButtonClick: tempDataProduk = ${produkData.produkIdList?.size}")
                    if(it.size<1 && produkData.produkIdList?.size==0){
                        Log.d(TAG, "onMinButtonClick: Masuk kesini?")
                        binding.llHeader.resetSwitch.isChecked = false
                        binding.llHeader.resetSwitch.isEnabled = false
                        binding.btnSelanjutnya.isEnabled = false
                        setBtnSelanjutnyaTint(false)
                        sharedViewModel.tempDataProduk.value?.produkIdList?.clear()
                        sharedViewModel.tempDataProduk.value?.produkNameList?.clear()
                        sharedViewModel.tempDataProduk.value?.produkQtyList?.clear()
                        sharedViewModel.tempDataProduk.value?.produkSatuanPerList?.clear()
                        sharedViewModel.tempDataProduk.value?.hargaProdukList?.clear()
                    }else{
                        Log.d(TAG, "onMinButtonClick: masuk else")
                        binding.llHeader.resetSwitch.isChecked = true
                        binding.llHeader.resetSwitch.isEnabled = true
                        binding.btnSelanjutnya.isEnabled = true
                        setBtnSelanjutnyaTint(true)

                    }
                }
            }
            else{
                Log.d(TAG, "onMinButtonClick: Masuk debug")
                for(i in produkData.produkIdList!!.indices){
                    if(i == produkData.produkIdList!!.indexOf(produk[position].id_produk) && counter>0){
                         sharedViewModel._tempDataPembayaran.value!![i] = DataPembayaran(produkData.produkIdList!![i]!!, produkData.produkNameList!![i]!!, produkData.produkQtyList!![i]!!,produkData.produkSatuanPerList!![i]!!, produkData.hargaProdukList!![i]!!)
                        viewModel.tempDataPembayaran[i] = DataPembayaran(produkData.produkIdList!![i]!!, produkData.produkNameList!![i]!!, produkData.produkQtyList!![i]!!,produkData.produkSatuanPerList!![i]!!, produkData.hargaProdukList!![i]!!)

                        if(binding.llHeader.btnSwitchLayout.background.equals(R.drawable.ic_linear_layout)){
                            Log.d(TAG, "onMinButtonClick: masuk")
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogTransaksiBinding.inflate(inflater, container, false)
        binding.totalHarga.text = getString(R.string.total_harga_arg,"0")
        toolbar = inflater.inflate(R.layout.toolbar, container, false) as Toolbar
        toolbar.title = getString(R.string.transaksi_produk)
        toolbar.collapseActionView()
        dataStorePreferences.currentDatePrefFlow.asLiveData()
            .observe(viewLifecycleOwner){ date->
                if (date != null) {
                    currentDate = date
                }
            }
        return binding.root
    }

    private fun setupFragmentContainer() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.addToBackStack(getString(R.string.dialog_pembayaran))
        transaction.replace(R.id.containerTransaksi,PembayaranDialog())
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        if(binding.btnSelanjutnya.visibility == View.VISIBLE) sharedViewModel.isBackPressed.value = -1
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedViewModel.tempDataProduk.value = null
        binding.llHeader.resetSwitch.visibility = View.VISIBLE
        binding.llHeader.tvReset.visibility = View.VISIBLE
        binding.llHeader.title.visibility = View.GONE
        if(binding.btnSelanjutnya.visibility == View.VISIBLE) sharedViewModel.isBackPressed.value = -1
        transaksiAdapter = TransaksiAdapter(viewModel.isLinearLayoutManager, handler)
        transaksiAdapter.setHasStableIds(true)
        transaksiAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW

        setupMenu()
        setupListeners()
        setupObservers()
        setupLayoutPreference()
    }

    private fun setupDataProdukRecyclerView() {
        transaksiAdapter = TransaksiAdapter(viewModel.isLinearLayoutManager, handler)
        transaksiAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        with(binding.recyclerViewDataTransaksi) {
            isNestedScrollingEnabled = true
            adapter = transaksiAdapter
            setHasFixedSize(true)
        }
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

    private fun setupListeners() {
        val prefix = getString(R.string.prefix)
        val invoice = prefix+generateUuid()
        lateinit var produkDibeli: String
        var jumlahProdukDibeli = -1
        with(binding) {
            btnSelanjutnya.enableOnClickAnimation()
            btnBatal.enableOnClickAnimation()
            btnSimpan.enableOnClickAnimation()
            if(viewModel.listTransaksi.value?.isNullOrEmpty() == null) {
                viewModel.insertTransaksi(TransaksiEntity(invoice,tanggal = getCurrentDate()))
            }
            btnSimpan.setOnClickListener {
                produkDibeli = ""
                for (i in sharedViewModel.tempDataProduk.value?.produkNameList!!.indices){
                    produkDibeli += getString(R.string.produk_dibeli_arg2,sharedViewModel.tempDataProduk.value?.produkNameList!![i],sharedViewModel.tempDataProduk.value?.produkQtyList!![i].toString(),sharedViewModel.tempDataProduk.value?.produkSatuanPerList!![i],sharedViewModel.tempDataProduk.value?.hargaProdukList!![i]!!.toRupiahFormatV2())
                }
                jumlahProdukDibeli = sharedViewModel.tempDataProduk.value?.produkIdList?.size!!
                val totalAkhir = if(sharedViewModel.totalDenganDiskon.value!=0.0) sharedViewModel.totalDenganDiskon.value!! else sharedViewModel._subTotal.value!!

                if(sharedViewModel.jumlahBayar.value!=0.0 && sharedViewModel._jumlahDiskon.value==0.0){
                    if(sharedViewModel.jumlahBayar.value!!<sharedViewModel._subTotal.value!!){
                        Toast.makeText(requireContext(), getString(R.string.kurang_dari_total_bayar), Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }

                if(totalHarga.text.toString().contains('-')){
                    Toast.makeText(requireContext(), getString(R.string.diskon_tidak_valid), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(sharedViewModel.jumlahBayar.value == 0.0) {
                    Toast.makeText(requireContext(), getString(R.string.isi_pembayaran_dulu), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(sharedViewModel._jumlahDiskon.value!=0.0){
                    if(sharedViewModel.jumlahBayar.value!!<totalAkhir) {
                        Toast.makeText(requireContext(), getString(R.string.jumlah_bayar_kurang_dari_total_bayar), Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                viewModel.insertTransaksi(TransaksiEntity(id_transaksi = invoice, tanggal = getCurrentDate()))
                viewModel.getListIdTransaksi()
                viewModel.insertTransaksiProduk()
                if(produkDibeli!=""){
                    Log.d(TAG, "setupListeners: masuk")
                    Log.d(TAG, "setupListeners: ${sharedViewModel.jenisPembayaran.value!!}")
                    if(jumlahProdukDibeli!=-1){
                        viewModel.insertHistoriTransaksi(HistoriTransaksiEntity(id_histori = invoice,
                            total = sharedViewModel._subTotal.value!!, diskon = sharedViewModel._jumlahDiskon.value!!, bayar = sharedViewModel.jumlahBayar.value!!, kembalian = sharedViewModel.jumlahKembalian.value!!,
                            catatan = sharedViewModel.catatan.value!!, produkDibeli = produkDibeli, jumlahProdukDibeli = jumlahProdukDibeli, pembayaran = sharedViewModel.jenisPembayaran.value!!, statusBayar = getString(R.string.lunas),tanggal = getCurrentDate()))
                        lifecycleScope.launch {
                            dataStorePreferences.saveDataUpdateSetting(requireContext(),true)
                        }

                        viewModel.getDataGrafik.observe(viewLifecycleOwner){
                            if(it.isNullOrEmpty()){
                                viewModel.insertDataGrafik(GrafikEntity(id = invoice, totalTransaksi = sharedViewModel._subTotal.value!!,System.currentTimeMillis(), bulanDanTahun = getCurrentMonthAndYear(), tanggal2 = getCurrentDate2()))
                                return@observe
                            }
                            var isExist = true
                            try {
                                for (i in it){
                                    isExist = i.tanggal2 == getCurrentDate2()
                                }
                            }catch (e:Exception){}
                            finally {
                                if(!isExist) viewModel.insertDataGrafik(GrafikEntity(id = invoice, totalTransaksi = sharedViewModel._subTotal.value!!,System.currentTimeMillis(), bulanDanTahun = getCurrentMonthAndYear(), tanggal2 = getCurrentDate2()))
                            }
                        }
                    }
                }
                Toast.makeText(requireContext(), "Transaksi Berhasil !", Toast.LENGTH_SHORT).show()
            }

            btnSelanjutnya.setOnClickListener {
                if(searchView.isVisible) {
                    searchView.onActionViewCollapsed()
                    searchItem.collapseActionView()
                    searchView.isVisible = false
                }
                setupFragmentContainer()
                sharedViewModel.isBackPressed.value = 3
                sharedViewModel.isSearchViewVisible.value = false
                Log.d(TAG, "setupListeners: ${sharedViewModel._tempDataPembayaran.value}")
                Log.d(TAG, "setupListeners: ${viewModel.tempDataPembayaran}")
                btnSelanjutnya.visibility = View.GONE
                llHeader.llHeader.visibility = View.GONE
                llHeader.resetSwitch.visibility = View.GONE
                llHeader.tvReset.visibility = View.GONE
                llHeader.title.visibility = View.VISIBLE
                swipeRefreshLayout.visibility = View.GONE
                btnBatal.visibility = View.VISIBLE
                btnSimpan.visibility = View.VISIBLE
            }

            llHeader.resetSwitch.setOnClickListener {
                if(llHeader.resetSwitch.isEnabled){
                    transaksiAdapter.updateData(null)
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
                    TransaksiAdapter.GridViewHolder.tempProduk.clear()
                    TransaksiAdapter.GridViewHolder.tempCounter.clear()
                    setupObservers()
                    sharedViewModel._tempDataPembayaran.value?.clear()
                    viewModel.tempDataPembayaran.clear()
                    sharedViewModel.tempDataProduk.value = null
                    llHeader.resetSwitch.isEnabled = false
                }else llHeader.resetSwitch.isEnabled = false
            }
            btnBatal.setOnClickListener{
                sharedViewModel.isBackPressed.value = -1
                sharedViewModel.isSearchViewVisible.value = true
                sharedViewModel.jumlahKembalian.value = 0.0
                sharedViewModel.catatan.value = ""
                btnBatal.visibility = View.GONE
                sharedViewModel.jumlahBayar.value = 0.0
                btnSimpan.visibility = View.GONE
                btnSelanjutnya.visibility = View.VISIBLE
                llHeader.llHeader.visibility = View.VISIBLE
                llHeader.resetSwitch.visibility = View.VISIBLE
                llHeader.tvReset.visibility = View.VISIBLE
                llHeader.title.visibility = View.GONE
                swipeRefreshLayout.visibility = View.VISIBLE
                childFragmentManager.popBackStack(getString(R.string.dialog_pembayaran), FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                observeTempDataProduk()
            }
            llHeader.btnSwitchLayout.setOnClickListener {
                transaksiAdapter.updateData(null)
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
                TransaksiAdapter.GridViewHolder.tempProduk.clear()
                TransaksiAdapter.GridViewHolder.tempCounter.clear()
                sharedViewModel._tempDataPembayaran.value?.clear()
                viewModel.tempDataPembayaran.clear()
                sharedViewModel.tempDataProduk.value = null
                llHeader.resetSwitch.isEnabled = false
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
                TransaksiAdapter.GridViewHolder.tempProduk.clear()
                TransaksiAdapter.GridViewHolder.tempCounter.clear()
                sharedViewModel._tempDataPembayaran.value?.clear()
                llHeader.resetSwitch.isChecked = false
                llHeader.resetSwitch.isEnabled = false
                viewModel.tempDataPembayaran.clear()
                setBtnSelanjutnyaTint(false)
                viewModel.dataPembayaran.value?.clear()
                sharedViewModel.tempDataProduk.value = null
                binding.btnSelanjutnya.isEnabled = false
            }
        }
    }

    private fun setupLayoutSwitcher() {
        if (viewModel.isLinearLayoutManager) binding.recyclerViewDataTransaksi.layoutManager =
            LinearLayoutManager(this.requireContext())
        else binding.recyclerViewDataTransaksi.layoutManager = GridLayoutManager(this.requireContext(), 2)
    }

    private fun setupLayoutSwitcherIcon() {
        if (viewModel.isLinearLayoutManager) binding.llHeader.btnSwitchLayout.setBackgroundResource(
            R.drawable.ic_linear_layout)
        else binding.llHeader.btnSwitchLayout.setBackgroundResource(R.drawable.ic_grid_layout)
    }


    @SuppressLint("FragmentBackPressedCallback")
    private fun setupObservers() {
        viewModel.isSuccess.observe(viewLifecycleOwner){
            if(it==true){
                TransaksiAdapter.LinearViewHolder.produkIdList = mutableListOf()
                TransaksiAdapter.LinearViewHolder.produkNameList = mutableListOf()
                TransaksiAdapter.LinearViewHolder.produkQtyList = mutableListOf()
                TransaksiAdapter.LinearViewHolder.produkSatuanPerList = mutableListOf()
                TransaksiAdapter.LinearViewHolder.produkPriceList = mutableListOf()
                TransaksiAdapter.LinearViewHolder.tempProduk = mutableListOf()
                TransaksiAdapter.LinearViewHolder.tempCounter = mutableListOf()
                TransaksiAdapter.GridViewHolder.produkIdList = mutableListOf()
                TransaksiAdapter.GridViewHolder.produkNameList = mutableListOf()
                TransaksiAdapter.GridViewHolder.produkQtyList = mutableListOf()
                TransaksiAdapter.GridViewHolder.produkSatuanPerList = mutableListOf()
                TransaksiAdapter.GridViewHolder.produkPriceList = mutableListOf()
                TransaksiAdapter.GridViewHolder.tempProduk = mutableListOf()
                TransaksiAdapter.GridViewHolder.tempCounter = mutableListOf()
                sharedViewModel._tempDataPembayaran.value = mutableListOf()
                viewModel.tempDataPembayaran = mutableListOf()
                viewModel.dataPembayaran.value?.clear()
                sharedViewModel.tempDataProduk.value = null
                viewModel.isNavReady.value = false
                sharedViewModel.jumlahKembalian.value = 0.0
                sharedViewModel.catatan.value = ""
                findNavController().popBackStack(R.id.transaksiDialog, true)
            }
        }

        sharedViewModel.jumlahDiskon.observe(viewLifecycleOwner){
            val hasil = (sharedViewModel._subTotal.value!!.minus(it))
            if(it!=0.0){
                if(hasil.toString().contains("-")){
                    binding.totalHarga.text = getString(R.string.total_harga_arg,hasil.toRupiahFormatV2())
                }else {
                    binding.totalHarga.text = hasil.toRupiahFormat()
                    sharedViewModel.totalDenganDiskon.value = hasil
                }

            }else{
                binding.totalHarga.text = sharedViewModel._subTotal.value!!.toRupiahFormat()
                sharedViewModel.hasilDiskon.value = null
                sharedViewModel.totalDenganDiskon.value = 0.0
            }
        }
        viewModel.isDataTransaksiEmpty.observe(viewLifecycleOwner) {
            binding.dataKosong.isVisible = it
        }
        viewModel.getDataProduk.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                viewModel.isDataTransaksiEmpty.value = it.size<1
                setupDataProdukRecyclerView()
                transaksiAdapter.setData(it)
                Log.d(TAG, "getDataProduk: ${it.size}")
                it.forEach { Log.d(TAG, "getDataProduk: $it")}
                setupLayoutSwitcher()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
        observeTempDataProduk()

    }

    private fun observeTempDataProduk() {
        sharedViewModel.tempDataProduk.observe(viewLifecycleOwner){it ->
            val empty = 0.0
            val total: Double? = it?.hargaProdukList?.sumOf { it ?: 0.0 }
            Log.d(TAG, "setupObservers: total = $total")
            binding.btnSelanjutnya.isEnabled = total!=null
            setBtnSelanjutnyaTint(binding.btnSelanjutnya.isEnabled)
            if(sharedViewModel.hasilDiskon.value==null){
                binding.totalHarga.text = total?.toRupiahFormat()?:empty.toRupiahFormat()
            }
        }
    }
    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                menu.findItem(R.id.action_search).isVisible = binding.btnSelanjutnya.visibility == View.VISIBLE
            }

            @SuppressLint("RestrictedApi")
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
                searchItem = menu.findItem(R.id.action_search)
                searchView = searchItem.actionView as SearchView
                sharedViewModel.isSearchViewVisible.observe(viewLifecycleOwner){
                    searchItem.isVisible = it
                }
                searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
                    .setBackgroundColor(Color.TRANSPARENT)
                searchView.queryHint = getString(R.string.pencarian)
                val searchAutoCompleteTextView = searchView.findViewById(resources.getIdentifier("search_src_text", "id",requireActivity().packageName)) as SearchView.SearchAutoComplete
                searchAutoCompleteTextView.threshold = 1
                searchView.imeOptions = EditorInfo.IME_ACTION_DONE
                toolbar.addView(searchView)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(text: String?): Boolean {

                        return false
                    }

                    override fun onQueryTextChange(text: String?): Boolean {
                        if (text!=null) {
                            searchProduk(text)
                        }else{
                            transaksiAdapter.setData(transaksiAdapter.produkList as ArrayList<ProdukEntity>)
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    android.R.id.home -> {
                        if(binding.btnSelanjutnya.visibility == View.GONE) {
                            childFragmentManager.popBackStack(getString(R.string.dialog_pembayaran),FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            with(binding){
                                btnBatal.visibility = View.GONE
                                btnSimpan.visibility = View.GONE
                                sharedViewModel.isSearchViewVisible.value = true
                                btnSelanjutnya.visibility = View.VISIBLE
                                llHeader.llHeader.visibility = View.VISIBLE
                                llHeader.resetSwitch.visibility = View.VISIBLE
                                llHeader.tvReset.visibility = View.VISIBLE
                                llHeader.title.visibility = View.GONE
                                swipeRefreshLayout.visibility = View.VISIBLE
                            }
                        }
                        else {
                            sharedViewModel.isBackPressed.value = -1
                            TransaksiAdapter.LinearViewHolder.produkIdList = mutableListOf()
                            TransaksiAdapter.LinearViewHolder.produkNameList = mutableListOf()
                            TransaksiAdapter.LinearViewHolder.produkQtyList = mutableListOf()
                            TransaksiAdapter.LinearViewHolder.produkSatuanPerList = mutableListOf()
                            TransaksiAdapter.LinearViewHolder.produkPriceList = mutableListOf()
                            TransaksiAdapter.LinearViewHolder.tempProduk = mutableListOf()
                            TransaksiAdapter.LinearViewHolder.tempCounter = mutableListOf()
                            TransaksiAdapter.GridViewHolder.produkIdList = mutableListOf()
                            TransaksiAdapter.GridViewHolder.produkNameList = mutableListOf()
                            TransaksiAdapter.GridViewHolder.produkQtyList = mutableListOf()
                            TransaksiAdapter.GridViewHolder.produkSatuanPerList = mutableListOf()
                            TransaksiAdapter.GridViewHolder.produkPriceList = mutableListOf()
                            TransaksiAdapter.GridViewHolder.tempProduk = mutableListOf()
                            TransaksiAdapter.GridViewHolder.tempCounter = mutableListOf()
                            sharedViewModel._tempDataPembayaran.value = mutableListOf()
                            viewModel.tempDataPembayaran = mutableListOf()
                            viewModel.dataPembayaran.value?.clear()
                            sharedViewModel.tempDataProduk.value = null
                            setBtnSelanjutnyaTint(false)
                            binding.btnSelanjutnya.isEnabled = false
                            findNavController().navigate(R.id.action_transaksiDialog_to_transaksiFragment)
                        }
                        return true
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun searchProduk(query: String?) {
        val searchQuery = "%$query%"
        viewModel.searchProduk(searchQuery).observe(viewLifecycleOwner){
            transaksiAdapter.updateData(it as ArrayList<ProdukEntity>?)
            viewModel.isDataTransaksiEmpty.value = it.isNullOrEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}