package org.d3ifcool.catok.ui.beranda.transaksi

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.catok.R
import org.d3ifcool.catok.databinding.DialogEditQuantityBinding
import org.d3ifcool.catok.ui.main.SharedViewModel

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
            setView(binding?.root)
        }
        return builder.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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