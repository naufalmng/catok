package org.d3ifcool.catok.core.data.source.local.entities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.source.local.model.Produk
import org.d3ifcool.catok.databinding.DialogEditQuantityBinding
import org.d3ifcool.catok.ui.beranda.transaksi.TransaksiAdapter
import org.d3ifcool.catok.ui.main.SharedViewModel
import java.util.ArrayList

class EditQuantityDialog() : DialogFragment() {

    private var _binding: DialogEditQuantityBinding? = null

    private val binding get() = _binding
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = layoutInflater
        _binding = DialogEditQuantityBinding.inflate(inflater,null, false)

        val builder = AlertDialog.Builder(requireContext()).apply {
            setTitle("Kuantitas Produk")
            setView(binding?.root)
            setPositiveButton(R.string.simpan){_,_->
                val qty = getData() ?: return@setPositiveButton
                sharedViewModel.tempQty.value = qty
            }
            setNegativeButton(R.string.batal){_,_ ->
                dismiss()
            }
        }
        return builder.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun getData(): Int? {
        if(binding?.qty?.text.toString().isEmpty()){
            showMessage("Qty belum ditulis")
            return null
        }
        return binding?.qty?.text.toString().toInt()
    }
    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.CENTER,0,0)
            show()
        }
    }

}