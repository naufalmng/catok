package org.d3ifcool.catok.ui.beranda.produk

import android.annotation.SuppressLint
import android.database.DataSetObserver
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.databinding.FragmentDataProdukBinding
import org.d3ifcool.catok.ui.main.MainActivity
import org.d3ifcool.catok.utils.enableOnClickAnimation
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception


@SuppressLint("ClickableViewAccessibility")
class DataProdukFragment : Fragment() {

    private var _binding: FragmentDataProdukBinding? = null
    private val binding get() = _binding!!
    private var isOnActionMode = false
    lateinit var toolbar: Toolbar


    private val viewModel: DataProdukViewModel by viewModel()
    private lateinit var produkAdapter: DataProdukAdapter

    private val dataStorePreferences: DataStorePreferences by lazy {
        DataStorePreferences(requireActivity().dataStore)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataProdukBinding.inflate(inflater, container, false)
//        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        toolbar = inflater.inflate(R.layout.toolbar, container, false) as Toolbar
        toolbar.title = getString(R.string.data_produk)
        toolbar.collapseActionView()
        if(binding.recyclerViewDataProduk.size<1) binding.dataKosong.visibility = View.VISIBLE else  binding.dataKosong.visibility = View.GONE

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(savedInstanceState!=null){
            if(produkAdapter.getSelection().isNotEmpty()){
                val activity = requireActivity() as MainActivity
                actionMode = activity.startSupportActionMode(actionModeCallback)
            }
        }
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
//        binding.llHeader.searchView.setupSearchView()
        setupListeners()
        setupObservers()
        setupLayoutPreference()
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {

            }

            @SuppressLint("RestrictedApi")
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem?.actionView as SearchView
                searchView.findViewById<View>(androidx.appcompat.R.id.search_plate).setBackgroundColor(Color.TRANSPARENT)
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
                        try {
                            produkAdapter.filter.filter(text.toString())
                        }catch (e: Exception){}
                        finally {
                            viewModel.isDataProdukEmpty.value = produkAdapter.currentList.isNullOrEmpty()

                        }

                        if(text?.isEmpty() == true){
                            produkAdapter.produkFilterList = produkAdapter.produkList
                            viewModel.isDataProdukEmpty.value = produkAdapter.currentList.isNullOrEmpty()
                        }
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    android.R.id.home -> {
                        findNavController().navigate(R.id.action_dataProdukFragment_to_berandaFragment)
                        return true
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerViews() {
        produkAdapter = DataProdukAdapter(viewModel.isLinearLayoutManager,handler)
        with(binding.recyclerViewDataProduk) {
            isNestedScrollingEnabled = true
            adapter = produkAdapter
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

    private fun setupObservers() {
        viewModel.isDataProdukEmpty.observe(viewLifecycleOwner) {
            if (it == false) {
                binding.dataKosong.visibility = View.GONE
//                binding.recyclerViewDataProduk.visibility = View.VISIBLE
            } else {
                binding.dataKosong.visibility = View.VISIBLE
//                binding.recyclerViewDataProduk.visibility = View.GONE
            }
        }
        viewModel.getDataProduk.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                viewModel.isDataProdukEmpty.value = true
                setupRecyclerViews()
                produkAdapter.updateData(it)

            }else{
                viewModel.isDataProdukEmpty.value = false
                setupRecyclerViews()
                produkAdapter.updateData(it)
                setupLayoutSwitcher()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
//            setupSearchView()
            btnTambah.enableOnClickAnimation()
            btnTambah.setOnClickListener {
                if (SystemClock.elapsedRealtime() - viewModel.mLastClickTime < 1000){
                    return@setOnClickListener
                }
                viewModel.mLastClickTime = SystemClock.elapsedRealtime()
                produkAdapter = DataProdukAdapter(viewModel.isLinearLayoutManager,handler)
                produkAdapter.resetSelection()
                setupObservers()
                actionMode?.finish()
//                llHeader.searchView.clearFocus()
//                llHeader.searchView.text?.clear()
                findNavController().navigate(DataProdukFragmentDirections.actionDataProdukFragmentToDataProdukDialog())
            }
            llHeader.btnSwitchLayout.setOnClickListener {
                viewModel.isLinearLayoutManager = !viewModel.isLinearLayoutManager
//                llHeader.searchView.text?.clear()
//                llHeader.searchView.clearFocus()
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
                if(recyclerViewDataProduk.size>0){
                    setupObservers()
                }else swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupLayoutSwitcherIcon() {
        if (viewModel.isLinearLayoutManager) binding.llHeader.btnSwitchLayout.setBackgroundResource(R.drawable.ic_linear_layout)
        else binding.llHeader.btnSwitchLayout.setBackgroundResource(R.drawable.ic_grid_layout)
    }

    private fun setupLayoutSwitcher() {
        if (viewModel.isLinearLayoutManager) binding.recyclerViewDataProduk.layoutManager = LinearLayoutManager(this.requireContext())
        else binding.recyclerViewDataProduk.layoutManager = GridLayoutManager(this.requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private var actionMode: ActionMode? = null
    private val actionModeCallback = object : ActionMode.Callback{
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            if(item?.itemId == R.id.menu_delete){
                deleteData()
                return true
            }
            if(item?.itemId == R.id.menu_select_all){
                if(viewModel.isAllItemSelected.value!=true) produkAdapter.getAllSelection()
                else {
                    produkAdapter.resetSelection()
                    viewModel.isAllItemSelected.value = false
                    actionMode!!.finish()
                    actionMode = null
                }
                return true
            }
            if(item?.itemId == R.id.menu_edit){
                produkAdapter.resetSelection()
                actionMode!!.finish()
                actionMode = null
                findNavController().navigate(DataProdukFragmentDirections.actionDataProdukFragmentToDataProdukDialog(false,viewModel.tempProdukEntity.value))
            }
            return false
        }
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.action_menu, menu)
            return true
        }
        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menu!!.findItem(R.id.menu_edit).isVisible = produkAdapter.getSelection().size == 1
            viewModel.isAllItemSelected.observe(viewLifecycleOwner){
                if(it==true) {
                    menu.findItem(R.id.menu_edit).isVisible = false
                }
                mode?.title = produkAdapter.getSelection().size.toString()
                menu.findItem(R.id.menu_select_all).isVisible = produkAdapter.getSelection().size >= 1
            }
            return true
        }
        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            produkAdapter.resetSelection()
        }
    }

    private fun deleteData() = AlertDialog.Builder(requireContext()).apply {
        var deleteMsg = -1
        var resultMsg = -1
        viewModel.isAllItemSelected.observe(viewLifecycleOwner){
            deleteMsg = if(it!=false) R.string.pesan_hapus_semua else R.string.pesan_hapus
            resultMsg = if(it!=false) R.string.berhasil_menghapus_semua_produk else R.string.berhasil_menghapus_produk
        }
        setMessage(deleteMsg)
        setPositiveButton(R.string.hapus) { _, _ ->
            if(resultMsg!=-1 && deleteMsg!=-1){
                viewModel.deleteData(produkAdapter.getSelection())
                produkAdapter.produkList.clear()
                produkAdapter.produkFilterList.clear()
                produkAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), resultMsg, Toast.LENGTH_SHORT).show()
                actionMode?.finish()
            }
        }
        setNegativeButton(R.string.batal) { dialog, _ ->
            dialog.cancel()
            actionMode?.finish()
        }
        show()
    }
    private val handler = object : DataProdukAdapter.ClickHandler{
        override fun onClick(position: Int, produk: ArrayList<ProdukEntity>) {
            if (actionMode != null) {
                viewModel.isAllItemSelected.value = produkAdapter.getSelection().size == produkAdapter.produkFilterList.size.minus(1)
                produkAdapter.toggleSelection(produkAdapter.produkList.indexOf(produkAdapter.produkFilterList[position]))
                if (produkAdapter.getSelection().isEmpty())
                    actionMode?.finish()
                else
                    actionMode?.invalidate()
                return
            }
            val message = getString(R.string.produk_klik, produk[position].namaProduk)
//            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
        override fun onLongClick(position: Int, produk: ArrayList<ProdukEntity>): Boolean {
            if(produkAdapter.getSelection().isEmpty()) viewModel.isAllItemSelected.value = false

            if(produkAdapter.getSelection().size == produkAdapter.produkFilterList.size.minus(1)){
                viewModel.isAllItemSelected.value = false
            }
            viewModel.tempProdukEntity.value = produk[position]
            if(actionMode != null) return false
            produkAdapter.toggleSelection(produkAdapter.produkList.indexOf(produkAdapter.produkFilterList[position]))
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

}