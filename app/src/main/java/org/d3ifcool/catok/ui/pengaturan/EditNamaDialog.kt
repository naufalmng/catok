package org.d3ifcool.catok.ui.pengaturan

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.catok.databinding.DialogEditNamaBinding
import org.d3ifcool.catok.ui.main.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditNamaDialog : DialogFragment() {

    private var _binding: DialogEditNamaBinding? = null
    private val binding get() = _binding!!
    private val pengaturanViewModel: PengaturanViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogEditNamaBinding.inflate(layoutInflater, null, false)
        val builder = AlertDialog.Builder(requireContext()).apply {
            setView(binding.root)
            binding.negativeBtn.setOnClickListener{
                dismiss()
            }
            binding.positiveBtn.setOnClickListener{
                if(binding.etNamaToko.text.toString().isEmpty()){
                    Toast.makeText(requireContext(), "Nama Toko Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                sharedViewModel.namaToko.value = binding.etNamaToko.text.toString()
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
}