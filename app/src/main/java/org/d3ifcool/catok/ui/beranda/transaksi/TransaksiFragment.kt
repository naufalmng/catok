package org.d3ifcool.catok.ui.beranda.transaksi

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
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
import org.d3ifcool.catok.core.data.source.local.entities.HistoriTransaksiEntity
import org.d3ifcool.catok.databinding.FragmentTransaksiBinding
import org.d3ifcool.catok.ui.main.MainActivity
import org.d3ifcool.catok.ui.main.SharedViewModel
import org.d3ifcool.catok.utils.enableOnClickAnimation
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransaksiFragment : Fragment() {

    private var _binding: FragmentTransaksiBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransaksiViewModel by viewModel()
    private lateinit var historiTransaksiAdapter: HistoriTransaksiAdapter
    private lateinit var dataPembayaranAdapter: DataPembayaranAdapter
    lateinit var toolbar: Toolbar

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val dataStorePreferences: DataStorePreferences by lazy {
        DataStorePreferences(requireActivity().dataStore)
    }

    private var actionMode: ActionMode? = null
    private val actionModeCallback = object : ActionMode.Callback{
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            if(item?.itemId == R.id.menu_delete){
                return true
            }
            if(item?.itemId == R.id.menu_select_all){
                if(viewModel.isAllItemSelected.value!=true) historiTransaksiAdapter.getAllSelection()
                else {
                    historiTransaksiAdapter.resetSelection()
                    viewModel.isAllItemSelected.value = false
                    actionMode!!.finish()
                    actionMode = null
                }
                return true
            }
            if(item?.itemId == R.id.menu_edit){
                historiTransaksiAdapter.resetSelection()
                actionMode!!.finish()
                actionMode = null
//                findNavController().navigate(DataProdukFragmentDirections.actionDataProdukFragmentToDataProdukDialog(false,viewModel.tempProdukEntity.value))
            }
            return false
        }
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.action_menu, menu)
            return true
        }
        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menu!!.findItem(R.id.menu_edit).isVisible = historiTransaksiAdapter.getSelection().size == 1
            viewModel.isAllItemSelected.observe(viewLifecycleOwner){
                if(it==true) menu.findItem(R.id.menu_edit).isVisible = false
                mode?.title = historiTransaksiAdapter.getSelection().size.toString()
            }
            return true
        }
        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            historiTransaksiAdapter.resetSelection()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val handler = object : HistoriTransaksiAdapter.ClickHandler{
        override fun onClick(position: Int, produk: ArrayList<HistoriTransaksiEntity>) {
            if (actionMode != null) {
                viewModel.isAllItemSelected.value = historiTransaksiAdapter.getSelection().size == historiTransaksiAdapter.historiHistoriTransaksiFilterList.size.minus(1)
                historiTransaksiAdapter.toggleSelection(historiTransaksiAdapter.historiTransaksiList.indexOf(historiTransaksiAdapter.historiHistoriTransaksiFilterList[position]))
                if (historiTransaksiAdapter.getSelection().isEmpty())
                    actionMode?.finish()
                else
                    actionMode?.invalidate()
                return
            }
            val message = getString(R.string.produk_klik, produk[position].invoice)
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }

        override fun onLongClick(position: Int, produk: ArrayList<HistoriTransaksiEntity>): Boolean {
            if(historiTransaksiAdapter.getSelection().size == historiTransaksiAdapter.historiHistoriTransaksiFilterList.size.minus(1)){
                viewModel.isAllItemSelected.value = historiTransaksiAdapter.getSelection().isNotEmpty()
            }
            viewModel.tempProdukEntity.value = produk[position]
            if(actionMode != null) return false
            historiTransaksiAdapter.toggleSelection(historiTransaksiAdapter.historiTransaksiList.indexOf(historiTransaksiAdapter.historiHistoriTransaksiFilterList[position]))
            val activity = requireActivity() as MainActivity
            actionMode = activity.startSupportActionMode(actionModeCallback)
            return true
        }

        override fun isAllItemSelected(isAllItemSelected: Boolean) {
            if(isAllItemSelected){
                viewModel.isAllItemSelected.value = isAllItemSelected
            }
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
        setupMenu()
        setupListeners()
        setupObservers()
        setupLayoutPreference()

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        sharedViewModel.isBackPressed.value = -1
    }

    private fun setupListeners() {
        with(binding) {
//            setupSearchView()
            btnTambah.enableOnClickAnimation()
            btnTambah.setOnClickListener {
//                if (SystemClock.elapsedRealtime() - viewModel.mLastClickTime < 1000){
//                    return@setOnClickListener
//                }
//                viewModel.mLastClickTime = SystemClock.elapsedRealtime();
                historiTransaksiAdapter.resetSelection()
                actionMode?.finish()
//                llHeader.searchView.clearFocus()
//                llHeader.searchView.text?.clear()
                findNavController().navigate(R.id.action_transaksiFragment_to_transaksiDialog)
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
        if(savedInstanceState!=null){
            historiTransaksiAdapter = HistoriTransaksiAdapter(viewModel.isLinearLayoutManager, handler)
            if(historiTransaksiAdapter.getSelection().isNotEmpty()){
                val activity = requireActivity() as MainActivity
                actionMode = activity.startSupportActionMode(actionModeCallback)
            }
        }
        super.onViewCreated(view, savedInstanceState)
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
                binding.recyclerView.visibility = View.VISIBLE
            } else {
                binding.dataKosong.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }
        viewModel.getDataHistoriTransaksi.observe(viewLifecycleOwner) {
            if (it != null) {
                setupRecyclerViews()
                historiTransaksiAdapter.updateData(it)
                setupLayoutSwitcher()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupRecyclerViews() {
        historiTransaksiAdapter = HistoriTransaksiAdapter(viewModel.isLinearLayoutManager, handler)
        with(binding.recyclerView) {
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
        if (viewModel.isLinearLayoutManager) binding.recyclerView.layoutManager =
            LinearLayoutManager(this.requireContext())
        else binding.recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
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
                            historiTransaksiAdapter.historiHistoriTransaksiFilterList =
                                historiTransaksiAdapter.historiTransaksiList
                            binding.dataKosong.visibility = View.GONE
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