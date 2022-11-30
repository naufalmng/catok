package org.d3ifcool.catok.ui.beranda.transaksi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.toSpannable
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.catok.R
import org.d3ifcool.catok.databinding.DialogPembayaranBinding
import org.d3ifcool.catok.ui.main.SharedViewModel
import org.d3ifcool.catok.utils.toRupiahFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class PembayaranDialog : Fragment() {
    companion object{
        private var TAG = PembayaranDialog.javaClass.name
    }

    private var _binding: DialogPembayaranBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: TransaksiViewModel by viewModel()
    private lateinit var pembayaranAdapter: DataPembayaranAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPembayaranBinding.inflate(inflater, container, false)

        setupRecyclerView()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
//        pembayaranAdapter.clearData()
    }

    private fun setupRecyclerView() {
        sharedViewModel.tempDataPembayaran.observe(viewLifecycleOwner){
            if(it!=null){
                var subTotal = 0.0
                for (i in it){
                    subTotal += i.harga
                }
                subTotal.toRupiahFormat()
                pembayaranAdapter = DataPembayaranAdapter()
                pembayaranAdapter.updateData(it)
                with(binding.recyclerViewPembayaran) {
                    isNestedScrollingEnabled = true
                    adapter = pembayaranAdapter
                    setHasFixedSize(true)
                }

                binding.subTotal.text = subTotal.toRupiahFormat().toSpannable()
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}