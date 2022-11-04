package org.d3ifcool.catok.ui.beranda.produk

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ActionMode
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.DataStorePreferences
import org.d3ifcool.catok.core.data.dataStore
import org.d3ifcool.catok.core.data.source.model.ProdukEntity
import org.d3ifcool.catok.databinding.FragmentDataProdukBinding
import org.d3ifcool.catok.ui.MainActivity
import org.d3ifcool.catok.ui.beranda.produk.DataProdukAdapter.Companion.selectionIds
import org.d3ifcool.catok.utils.enableOnClickAnimation
import org.d3ifcool.catok.utils.setupSearchView
import org.koin.androidx.viewmodel.ext.android.viewModel
@SuppressLint("ClickableViewAccessibility")
class DataProdukFragment : Fragment() {

    private var _binding: FragmentDataProdukBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DataProdukViewModel by viewModel()
    private lateinit var produkAdapter: DataProdukAdapter

    private val dataStorePreferences: DataStorePreferences by lazy {
        DataStorePreferences(requireActivity().dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataProdukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.llHeader.tvListProduk.text = getString(R.string.list_produk)
        binding.llHeader.searchView.setupSearchView()
        setupListeners()
        setupObservers()
        setupLayoutPreference()
    }

    private fun setupRecyclerViews() {
        produkAdapter = DataProdukAdapter(viewModel.isLinearLayoutManager, handler)
        with(binding.recyclerView) {
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
                setupSearchViewFilter()
            }
    }

    private fun setupObservers() {
        viewModel.isDataProdukEmpty.observe(viewLifecycleOwner) {
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
                setupRecyclerViews()
                produkAdapter.updateData(it)
                setupLayoutSwitcher()
                setupSearchViewFilter()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupSearchViewFilter() {
        binding.llHeader.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val searchQuery = binding.llHeader.searchView.text.toString()
                produkAdapter.filter.filter(searchQuery)
            }
        })
    }

    private fun setupListeners() {
        with(binding) {
            setupSearchView()
            btnTambah.enableOnClickAnimation()
            btnTambah.setOnClickListener {
                findNavController().navigate(R.id.action_dataProdukFragment_to_insertProdukDialog)
            }
            llHeader.btnSwitchLayout.setOnClickListener {
                viewModel.isLinearLayoutManager = !viewModel.isLinearLayoutManager
                llHeader.searchView.text?.clear()
                llHeader.searchView.clearFocus()
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

    private fun setupSearchView() {
        with(binding.llHeader.searchView){
            setOnFocusChangeListener { view, b ->
                if(view.hasFocus()){
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon_selector,0,R.drawable.ic_baseline_close_24,0)
                    produkAdapter.resetSelection()
                    actionMode?.finish()
                    actionMode = null
                }else {
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon_selector,0,0,0)
                    view.clearFocus()
                }
            }
            doOnTextChanged { text, start, before, count ->
                if(count>=1){
                    produkAdapter.resetSelection()
                    actionMode?.finish()
                    actionMode = null
                }
            }
            setOnTouchListener(View.OnTouchListener { v, event ->
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                var hasConsumed = false
                if (event.x >= width - totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        v.clearFocus()
                        text?.clear()
                        setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.search_icon_selector,
                            0,
                            0,
                            0
                        )
                        imm.hideSoftInputFromWindow(windowToken, 0)
                        produkAdapter.resetSelection()
                        actionMode?.finish()
                        actionMode = null
                        return@OnTouchListener true
                    } else setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.search_icon_selector,
                        0,
                        R.drawable.ic_baseline_close_24,
                        0
                    )

                    hasConsumed = true
                }
                hasConsumed
            })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private var actionMode: ActionMode? = null
    private val actionModeCallback =object : ActionMode.Callback{
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
            return false
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.action_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            viewModel.isAllItemSelected.observe(viewLifecycleOwner){
                mode?.title = produkAdapter.getSelection().size.toString()
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
                viewModel.isAllItemSelected.value = produkAdapter.getSelection().size == produkAdapter.produkList.minus(1).size
                produkAdapter.toggleSelection(produkAdapter.produkList.indexOf(produkAdapter.produkFilterList[position]))
                if (produkAdapter.getSelection().isEmpty())
                    actionMode?.finish()
                else
                    actionMode?.invalidate()
                return
            }
            val message = getString(R.string.produk_klik, produk[position].namaProduk)
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
        override fun onLongClick(position: Int, produk: ArrayList<ProdukEntity>): Boolean {
            viewModel.isAllItemSelected.value = produkAdapter.getSelection().size == produkAdapter.produkList.minus(1).size
            if(actionMode != null) return false
            produkAdapter.toggleSelection(produkAdapter.produkList.indexOf(produkAdapter.produkFilterList[position]))
//            Toast.makeText(requireContext(), "item ${produk.indexOf(item)} is ${item.namaProduk}", Toast.LENGTH_SHORT).show()
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