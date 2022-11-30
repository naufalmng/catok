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
import kotlinx.coroutines.launch
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.DataStorePreferences
import org.d3ifcool.catok.core.data.dataStore
import org.d3ifcool.catok.core.data.source.local.entities.EditQuantityDialog
import org.d3ifcool.catok.core.data.source.local.model.Produk
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.core.data.source.local.model.DataPembayaran
import org.d3ifcool.catok.databinding.DialogTransaksiBinding
import org.d3ifcool.catok.ui.main.SharedViewModel
import org.d3ifcool.catok.utils.enableOnClickAnimation
import org.d3ifcool.catok.utils.getCurrentDate
import org.d3ifcool.catok.utils.toRupiahFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.log

class TransaksiDialog : Fragment() {

//    var list = listOf<String>("Momogi", "Dylan", "Cepmek")
//    for((index,value) in list.withIndex()){
//        println("${index.plus(1)}.$value")
//    }
    private val TAG = this.javaClass.name
    private var _binding: DialogTransaksiBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransaksiViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }
    private lateinit var transaksiAdapter: TransaksiAdapter
    private val pembayaranAdapter: DataPembayaranAdapter by lazy {
        DataPembayaranAdapter()
    }
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
            var dataPembayaran : MutableList<DataPembayaran> = mutableListOf()
            for(i in produkData.produkIdList!!.indices){
                dataPembayaran.add(i,DataPembayaran(produkData.produkIdList!![i]!!, produkData.produkNameList!![i]!!, produkData.produkQtyList!![i]!!, produkData.hargaProdukList!![i]!!))
            }
            sharedViewModel._tempDataPembayaran.value = dataPembayaran
            Log.d(TAG, "onPlusButtonClick: ${sharedViewModel.tempDataProduk.value}")
            binding.llHeader.resetSwitch.isChecked = true
            binding.llHeader.resetSwitch.isEnabled = true

//            for(i in produkData.produkIdList!!.indices){
////                val dataPembayaran = DataPembayaran(produkData.produkIdList!![i]!!, produkData.produkNameList!![i]!!, produkData.produkQtyList!![i]!!, produkData.hargaProdukList!![i]!!)
////                sharedViewModel._tempDataPembayaran.value?.add(produkData.produkIdList!!.indexOf(produk[position].id_produk),dataPembayaran)
//                sharedViewModel.itemPos.value = produkData.produkIdList!!.indexOf(produk[position].id_produk)
//                sharedViewModel.qty.value = counter
//
////                Log.d(TAG, "onPlusButtonClick: Masuk ${produkData.produkIdList!!.indexOf(produk[position].id_produk)}")
//
////                if(produkData.produkIdList!![i] == sharedViewModel._tempDataPembayaran.value.indexOf()){
////                    Log.d(TAG, "onPlusButtonClick: Masuk")
////                }else{
////                    sharedViewModel._tempDataPembayaran.value?.add(produkData.produkIdList!!.indexOf(produk[position].id_produk),dataPembayaran)
////                }
//
//            }
//            if(produk[position].namaProduk ?.contains() == true){
//                sharedViewModel._tempDataPembayaran.value?.add(dataPembayaran.nomor,dataPembayaran)
//            }else sharedViewModel._tempDataPembayaran.value?.add(dataPembayaran)
            if(counter==1){
                setBtnSelanjutnyaTint(true)
            }
            if (counter > 1) {
                setBtnSelanjutnyaTint(true)
//                for(i in produkData.produkIdList!!.indices){
//                    val dataPembayaran = DataPembayaran(produkData.produkIdList!![i]!!, produkData.produkNameList!![i]!!, produkData.produkQtyList!![i]!!, produkData.hargaProdukList!![i]!!)
//                    if(sharedViewModel._tempDataPembayaran.value!![produkData.produkIdList!!.indexOf(produk[position].id_produk)].nomor == dataPembayaran.nomor){
//                        sharedViewModel._tempDataPembayaran.value?.add(produkData.produkIdList!!.indexOf(produk[position].id_produk),dataPembayaran)
//                    }
//                }
            }
        }


        override fun onMinButtonClick(position: Int, produk: ArrayList<ProdukEntity>,counter: Int,produkData: Produk) {
            var dataPembayaran : MutableList<DataPembayaran> = mutableListOf()

//            transaksiAdapter.toggleSelection(position)
            sharedViewModel.tempDataProduk.value = produkData
            Log.d(TAG, "onMinButtonClick: ${sharedViewModel.tempDataProduk.value}")
//            if(counter == 0 && produkData.produkIdList?.isNotEmpty() == true ){
//                sharedViewModel.tempDataProduk.value?.produkIdList?.clear()
//                sharedViewModel.tempDataProduk.value?.produkNameList?.clear()
//                sharedViewModel.tempDataProduk.value?.produkQtyList?.clear()
//                sharedViewModel.tempDataProduk.value?.hargaProdukList?.clear()
//                produkData.produkIdList?.clear()
//                produkData.produkNameList?.clear()
//                produkData.produkQtyList?.clear()
//                produkData.hargaProdukList?.clear()
//                binding.llHeader.resetSwitch.isChecked = false
//                binding.llHeader.resetSwitch.isEnabled = false
//                binding.btnSelanjutnya.isEnabled = false
//                setBtnSelanjutnyaTint(false)
//            }
            if(counter==0){
                sharedViewModel._tempDataPembayaran.value!!.removeLastOrNull()
                sharedViewModel.tempDataProduk.value?.produkIdList?.removeLastOrNull()
                sharedViewModel.tempDataProduk.value?.produkNameList?.removeLastOrNull()
                sharedViewModel.tempDataProduk.value?.produkQtyList?.removeLastOrNull()
                sharedViewModel.tempDataProduk.value?.hargaProdukList?.removeLastOrNull()

                Log.d(TAG, "= 0 onMinButtonClick: masuk sini?")
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
                        sharedViewModel.tempDataProduk.value?.hargaProdukList?.clear()
                    }else{
                        Log.d(TAG, "onMinButtonClick: masuk else")
                        binding.llHeader.resetSwitch.isChecked = true
                        binding.llHeader.resetSwitch.isEnabled = true
                        binding.btnSelanjutnya.isEnabled = true
                        setBtnSelanjutnyaTint(true)

                    }
                }

//                else sharedViewModel._tempDataPembayaran.value!!.removeAt(TransaksiAdapter.GridViewHolder.produkIdBeforeDeleted.value!!)
//                sharedViewModel._tempDataPembayaran.value!!.removeAt()
//                for(i in produkData.produkIdList!!.indices){
//                    if(i == produkData.produkIdList!!.indexOf(produk[position].id_produk)){
//                        sharedViewModel._tempDataPembayaran.value!!.removeAt(i)
//                    }
////                    dataPembayaran.remove(i,DataPembayaran(produkData.produkIdList!![i]!!, produkData.produkNameList!![i]!!, produkData.produkQtyList!![i]!!, produkData.hargaProdukList!![i]!!))

//                binding.btnSelanjutnya.isEnabled = false
//                transaksiAdapter.resetSelection()
            }else{
                Log.d(TAG, "onMinButtonClick: Masuk debug")
                for(i in produkData.produkIdList!!.indices){
                    if(i == produkData.produkIdList!!.indexOf(produk[position].id_produk) && counter>0){
                         sharedViewModel._tempDataPembayaran.value!![i] = DataPembayaran(produkData.produkIdList!![i]!!, produkData.produkNameList!![i]!!, produkData.produkQtyList!![i]!!, produkData.hargaProdukList!![i]!!)
                        if(binding.llHeader.btnSwitchLayout.background.equals(R.drawable.ic_linear_layout)){
                            Log.d(TAG, "onMinButtonClick: masuk")
//                            TransaksiAdapter.LinearViewHolder.produkIdList.removeAt(i)
                        }
//                        else TransaksiAdapter.GridViewHolder.produkIdList.removeAt(i)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogTransaksiBinding.inflate(inflater, container, false)
        if(binding.btnSelanjutnya.visibility == View.VISIBLE) sharedViewModel.isBackPressed.value = -1
        binding.totalHarga.text = getString(R.string.total_harga_arg,"0")
        toolbar = inflater.inflate(R.layout.toolbar, container, false) as Toolbar
        toolbar.title = getString(R.string.transaksi_produk)
        toolbar.collapseActionView()
        sharedViewModel.tempDataProduk.value = null
        binding.llHeader.resetSwitch.visibility = View.VISIBLE
        binding.llHeader.tvReset.visibility = View.VISIBLE
        binding.llHeader.title.visibility = View.GONE
        setupMenu()
        setupListeners()
        setupObservers()
        setupLayoutPreference()
        return binding.root
    }

    private fun setupFragmentContainer() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.addToBackStack("dialogPembayaran")
        transaction.replace(R.id.containerTransaksi,PembayaranDialog())
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        if(binding.btnSelanjutnya.visibility == View.VISIBLE) sharedViewModel.isBackPressed.value = -1
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(savedInstanceState!=null){
            transaksiAdapter = TransaksiAdapter(viewModel.isLinearLayoutManager, handler)
        }
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setupDataProdukRecyclerView() {
        transaksiAdapter = TransaksiAdapter(viewModel.isLinearLayoutManager, handler)
        with(binding.recyclerView) {
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
        with(binding) {
//            setupSearchView()
            btnSelanjutnya.enableOnClickAnimation()
            btnSelanjutnya.setOnClickListener {
                setupFragmentContainer()
                sharedViewModel.isBackPressed.value = 3
                Log.d(TAG, "setupListeners: ${sharedViewModel._tempDataPembayaran.value}")
                btnSelanjutnya.visibility = View.GONE
                llHeader.llHeader.visibility = View.GONE
                llHeader.resetSwitch.visibility = View.GONE
                llHeader.tvReset.visibility = View.GONE
                llHeader.title.visibility = View.VISIBLE
                swipeRefreshLayout.visibility = View.GONE
                btnBatal.visibility = View.VISIBLE
                btnSimpan.visibility = View.VISIBLE
//                val produkData = sharedViewModel.tempDataProduk.value
                
//                if(sharedViewModel.qty.value!! <= 1 ){
//                    for(i in produkData?.produkIdList!!.indices){
//                        val dataPembayaran = DataPembayaran(produkData.produkIdList!![i]!!, produkData.produkNameList!![i]!!, produkData.produkQtyList!![i]!!, produkData.hargaProdukList!![i]!!)
//                        sharedViewModel._tempDataPembayaran.value?.add(dataPembayaran)
//                    }
//                }
//                if(sharedViewModel.qty.value!!>1){
//                    for(i in produkData?.produkIdList!!.indices){
//                        val dataPembayaran = DataPembayaran(produkData.produkIdList!![i]!!, produkData.produkNameList!![i]!!, produkData.produkQtyList!![i]!!, produkData.hargaProdukList!![i]!!)
//                        sharedViewModel._tempDataPembayaran.value?.add(sharedViewModel.itemPos.value!!,dataPembayaran)
//                    }
//                }

//                if(pembayaranAdapter.itemCount==0) observeTempDataProduk()
//                viewModel.setupDataPembayaran()
//                pembayaranAdapter.updateData(viewModel.dataPembayaran.value!!)
//                prepareRecyclerViewPembayaran()
//                llHeader.searchView.clearFocus()
//                llHeader.searchView.text?.clear()
//                findNavController().navigate(R.id.action_dataProdukFragment_to_dataProdukDialog)
            }

            llHeader.resetSwitch.setOnClickListener {
                if(llHeader.resetSwitch.isEnabled){
                    transaksiAdapter.updateData(null)
                    TransaksiAdapter.LinearViewHolder.produkIdList.clear()
                    TransaksiAdapter.LinearViewHolder.produkNameList.clear()
                    TransaksiAdapter.LinearViewHolder.produkQtyList.clear()
                    TransaksiAdapter.LinearViewHolder.produkPriceList.clear()
                    TransaksiAdapter.GridViewHolder.produkIdList.clear()
                    TransaksiAdapter.GridViewHolder.produkNameList.clear()
                    TransaksiAdapter.GridViewHolder.produkQtyList.clear()
                    TransaksiAdapter.GridViewHolder.produkPriceList.clear()
                    setupObservers()
                    sharedViewModel._tempDataPembayaran.value?.clear()
                    sharedViewModel.tempDataProduk.value = null
                    llHeader.resetSwitch.isEnabled = false
                }else llHeader.resetSwitch.isEnabled = false
            }
            btnBatal.setOnClickListener{
                sharedViewModel.isBackPressed.value = -1
                btnBatal.visibility = View.GONE
                btnSimpan.visibility = View.GONE
                btnSelanjutnya.visibility = View.VISIBLE
                llHeader.llHeader.visibility = View.VISIBLE
                llHeader.resetSwitch.visibility = View.VISIBLE
                llHeader.tvReset.visibility = View.VISIBLE
                llHeader.title.visibility = View.GONE
                swipeRefreshLayout.visibility = View.VISIBLE
                childFragmentManager.popBackStack("dialogPembayaran", FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                observeTempDataProduk()
            }
            llHeader.btnSwitchLayout.setOnClickListener {
                transaksiAdapter.updateData(null)
                TransaksiAdapter.LinearViewHolder.produkIdList.clear()
                TransaksiAdapter.LinearViewHolder.produkNameList.clear()
                TransaksiAdapter.LinearViewHolder.produkQtyList.clear()
                TransaksiAdapter.LinearViewHolder.produkPriceList.clear()
                TransaksiAdapter.GridViewHolder.produkIdList.clear()
                TransaksiAdapter.GridViewHolder.produkNameList.clear()
                TransaksiAdapter.GridViewHolder.produkQtyList.clear()
                TransaksiAdapter.GridViewHolder.produkPriceList.clear()
                sharedViewModel._tempDataPembayaran.value?.clear()
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
                TransaksiAdapter.GridViewHolder.produkIdList.clear()
                TransaksiAdapter.GridViewHolder.produkNameList.clear()
                TransaksiAdapter.GridViewHolder.produkQtyList.clear()
                TransaksiAdapter.GridViewHolder.produkPriceList.clear()
                sharedViewModel._tempDataPembayaran.value?.clear()
                setBtnSelanjutnyaTint(false)
                viewModel.dataPembayaran.value?.clear()
                sharedViewModel.tempDataProduk.value = null
                binding.btnSelanjutnya.isEnabled = false
            }
        }
    }

    private fun setupLayoutSwitcher() {
        if (viewModel.isLinearLayoutManager) binding.recyclerView.layoutManager =
            LinearLayoutManager(this.requireContext())
        else binding.recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
    }

    private fun setupLayoutSwitcherIcon() {
        if (viewModel.isLinearLayoutManager) binding.llHeader.btnSwitchLayout.setBackgroundResource(
            R.drawable.ic_linear_layout)
        else binding.llHeader.btnSwitchLayout.setBackgroundResource(R.drawable.ic_grid_layout)
    }


    @SuppressLint("FragmentBackPressedCallback")
    private fun setupObservers() {
        viewModel.isDataTransaksiEmpty.observe(viewLifecycleOwner) {
            if (it == false) {
                binding.dataKosong.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            } else {
                binding.dataKosong.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }
        viewModel.getDataProduk.observe(viewLifecycleOwner) {

            if (it != null) {
                viewModel.isDataTransaksiEmpty.value = it.size<1
                setupDataProdukRecyclerView()
                transaksiAdapter.updateData(it)
                setupLayoutSwitcher()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
        observeTempDataProduk()

    }

    private fun observeTempDataProduk() {
        sharedViewModel.tempDataProduk.observe(viewLifecycleOwner){it ->
            var empty = 0.0
            var total: Double? = it?.hargaProdukList?.sumOf { it ?: 0.0 }
            Log.d(TAG, "setupObservers: total = $total")
            binding.btnSelanjutnya.isEnabled = total!=null
            setBtnSelanjutnyaTint(binding.btnSelanjutnya.isEnabled)
//            binding.llHeader.resetSwitch.isChecked = total!=0.0
            binding.totalHarga.text = total?.toRupiahFormat()?:empty.toRupiahFormat()
//            if(it?.produkIdList?.isNotEmpty() == true){
//                Log.d(TAG, "setupObservers: ${it.produkIdList!!}")
//                for (i in it.produkIdList!!.indices) {
//                    sharedViewModel._tempDataPembayaran.add(DataPembayaran(it.produkIdList!![i]!!, it.produkNameList!![i]!!, it.produkQtyList!![i]!!, it.hargaProdukList!![i]!!))
//                }
//            }
        }
    }
    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                menu.findItem(R.id.action_search).isVisible = binding.btnSelanjutnya.visibility == View.VISIBLE
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
                        transaksiAdapter.filter.filter(text.toString())
                        if (text?.isNotEmpty() == true) {
                            binding.dataKosong.visibility = View.VISIBLE
                            if (transaksiAdapter.produkFilterList.size <= 0) {
                                binding.dataKosong.visibility = View.VISIBLE
                            } else {
                                binding.dataKosong.visibility = View.GONE
                            }
                        } else {
                            transaksiAdapter.produkFilterList = transaksiAdapter.produkList
                            binding.dataKosong.visibility = View.GONE
                        }

                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    android.R.id.home -> {
                        if(binding.btnSelanjutnya.visibility == View.GONE) {
                            childFragmentManager.popBackStack("dialogPembayaran",FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            with(binding){
                                btnBatal.visibility = View.GONE
                                btnSimpan.visibility = View.GONE
                                btnSelanjutnya.visibility = View.VISIBLE
                                llHeader.llHeader.visibility = View.VISIBLE
                                llHeader.resetSwitch.visibility = View.VISIBLE
                                llHeader.tvReset.visibility = View.VISIBLE
                                llHeader.title.visibility = View.GONE
                                swipeRefreshLayout.visibility = View.VISIBLE
                            }
                        }else {
                            sharedViewModel.isBackPressed.value = -1
                            TransaksiAdapter.LinearViewHolder.produkIdList.clear()
                            TransaksiAdapter.LinearViewHolder.produkNameList.clear()
                            TransaksiAdapter.LinearViewHolder.produkQtyList.clear()
                            TransaksiAdapter.LinearViewHolder.produkPriceList.clear()
                            TransaksiAdapter.GridViewHolder.produkIdList.clear()
                            TransaksiAdapter.GridViewHolder.produkNameList.clear()
                            TransaksiAdapter.GridViewHolder.produkQtyList.clear()
                            TransaksiAdapter.GridViewHolder.produkPriceList.clear()
                            sharedViewModel._tempDataPembayaran.value?.clear()
                            setBtnSelanjutnyaTint(false)
                            viewModel.dataPembayaran.value?.clear()
                            sharedViewModel.tempDataProduk.value = null
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

    override fun onDestroyView() {
        super.onDestroyView()
//        TransaksiAdapter.LinearViewHolder.produkIdList.clear()
//        TransaksiAdapter.LinearViewHolder.produkNameList.clear()
//        TransaksiAdapter.LinearViewHolder.produkQtyList.clear()
//        TransaksiAdapter.LinearViewHolder.produkPriceList.clear()
//        TransaksiAdapter.GridViewHolder.produkIdList.clear()
//        TransaksiAdapter.GridViewHolder.produkNameList.clear()
//        TransaksiAdapter.GridViewHolder.produkQtyList.clear()
//        TransaksiAdapter.GridViewHolder.produkPriceList.clear()
//        sharedViewModel._tempDataPembayaran.value?.clear()
//        sharedViewModel.tempDataProduk.value = null
        _binding = null

    }
}