package org.d3ifcool.catok.ui.beranda.transaksi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.d3ifcool.catok.R
import org.d3ifcool.catok.databinding.DialogPembayaranBinding
import org.d3ifcool.catok.ui.main.SharedViewModel
import org.d3ifcool.catok.utils.toRupiahFormat

class PembayaranDialog : Fragment() {
    companion object{
        private var TAG = PembayaranDialog::class.java.name
    }

    private var _binding: DialogPembayaranBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var pembayaranAdapter: DataPembayaranAdapter
    private var subTotal = 0.0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPembayaranBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun setupListeners() {
        with(binding){
            spinnerJenisPembayaran.setOnSpinnerItemSelectedListener<String>{ oldIndex, oldItem, newIndex, text ->
                binding.tilJenisPembayaran.editText?.setText(text)
                sharedViewModel.jenisPembayaran.value = text
            }
            etCatatan.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    sharedViewModel.catatan.value = binding.etCatatan.text.toString()
                }
            })
            etKembalian.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    sharedViewModel.jumlahKembalian.value = binding.etKembalian.getNumericValue()
                    Log.d(TAG, "kembalian: ${binding.etKembalian.getNumericValue()}")
                }
            })
            etBayar.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if(etBayar.text.toString()!="" ){
                        sharedViewModel.jumlahBayar.value = etBayar.getNumericValue()
                        sharedViewModel.totalDenganDiskon.observe(viewLifecycleOwner){
                            if(it!=0.0){
                                if(etBayar.getNumericValue()>it){
                                    binding.etKembalian.setText(etBayar.getNumericValue().minus(it).toString().dropLast(2))
                                }else{
                                    binding.etKembalian.setText(0.0.toString())
                                }
                            }else{
                                if(etBayar.getNumericValue() > sharedViewModel._subTotal.value!!){
                                    Log.d(TAG, "afterTextChanged: ${(etBayar.getNumericValue()-sharedViewModel._subTotal.value!!).toString().dropLast(2)}")
                                    binding.etKembalian.setText((etBayar.getNumericValue() - sharedViewModel._subTotal.value!!).toString().dropLast(2))
                                }else{
                                    binding.etKembalian.setText(0.0.toString())
                                }
                            }
                        }
                    }
                }
            })
            if(etDiskon.hasFocus()){
                etBayar.setText("")
                etKembalian.setText("")
                sharedViewModel.jumlahBayar.value = 0.0
            }
        }
        binding.etDiskon.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d(TAG, "afterTextChanged: ${binding.etKembalian.getNumericValue()}")
                if(binding.etDiskon.text.toString()!="" ){
                    lifecycleScope.launch {
                        if(binding.etDiskon.getNumericValue()!=0.0){
                            binding.jumlahDiskon.text = binding.etDiskon.getNumericValue().toRupiahFormat()
                            binding.total.text = (subTotal-binding.etDiskon.getNumericValue()).toRupiahFormat()
                            sharedViewModel._jumlahDiskon.value = binding.etDiskon.getNumericValue()
                        }else{
                            sharedViewModel._jumlahDiskon.value = binding.etDiskon.getNumericValue()
                            binding.total.text = (subTotal-binding.etDiskon.getNumericValue()).toRupiahFormat()
                            Log.d(TAG, "afterTextChanged: ${binding.etKembalian.getNumericValue()}")
                            binding.jumlahDiskon.text = 0.0.toRupiahFormat()

                        }
                    }
                }
            }
        })

    }


    private fun setupRecyclerView() {
        sharedViewModel.tempDataPembayaran.observe(viewLifecycleOwner){
            if(it!=null){
                for (i in it){
                    subTotal += i.harga
                }
                sharedViewModel._subTotal.value = subTotal
                binding.total.text = (subTotal-binding.etDiskon.getNumericValue()).toRupiahFormat()
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
        binding.spinnerJenisPembayaran.selectItemByIndex(0)
        binding.jumlahDiskon.text = (0.0).toRupiahFormat()
        sharedViewModel.jenisPembayaran.value = when(binding.spinnerJenisPembayaran.selectedIndex){
            0 -> resources.getStringArray(R.array.item_jenis_pembayaran)[0]
            else -> resources.getStringArray(R.array.item_jenis_pembayaran)[1]
        }
        Log.d(TAG, "setupListeners: ${sharedViewModel.jenisPembayaran.value}")
        setupListeners()
        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}