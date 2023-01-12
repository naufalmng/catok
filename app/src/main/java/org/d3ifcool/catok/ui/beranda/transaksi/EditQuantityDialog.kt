package org.d3ifcool.catok.ui.beranda.transaksi

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import org.d3ifcool.catok.databinding.DialogEditQuantityBinding

class EditQuantityDialog : DialogFragment() {

    private var _binding: DialogEditQuantityBinding? = null

    private val binding get() = _binding

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

}