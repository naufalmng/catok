package org.d3ifcool.catok.ui.beranda.transaksi

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import org.d3ifcool.catok.R
import org.d3ifcool.catok.databinding.DialogDetailHistoriTransaksiBinding
import org.d3ifcool.catok.utils.addUnderline
import org.d3ifcool.catok.utils.toRupiahFormat
import java.util.*


class DetailHistoriTransaksiDialog : DialogFragment() {

    private var _binding: DialogDetailHistoriTransaksiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(true)
        _binding = DialogDetailHistoriTransaksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateUi()
    }

    private fun updateUi() {
        val data: DetailHistoriTransaksiDialogArgs by navArgs()
        with(binding){
            tanggal.text = getString(R.string.detail_transaksi_arg,data.transaksi.tanggal)
            invoice.text = getString(R.string.detail_transaksi_arg,data.transaksi.invoice)
            subTotal.text = getString(R.string.detail_transaksi_arg,data.transaksi.total.toRupiahFormat().addUnderline())
            total.text = getString(R.string.detail_transaksi_arg,(data.transaksi.total-data.transaksi.diskon).toRupiahFormat().uppercase(Locale("id","ID")).addUnderline())
            produkDibeli.text = getString(R.string.detail_transaksi_arg,data.transaksi.produkDibeli.replace(".",","))
            diskon.text = getString(R.string.detail_transaksi_arg,data.transaksi.diskon.toRupiahFormat())
            bayar.text = getString(R.string.detail_transaksi_arg,data.transaksi.bayar.toRupiahFormat())
            kembalian.text = getString(R.string.detail_transaksi_arg,data.transaksi.kembalian.toRupiahFormat())
            pembayaran.text = getString(R.string.detail_transaksi_arg,data.transaksi.pembayaran.addUnderline())
            catatan.text = getString(R.string.detail_transaksi_arg,data.transaksi.catatan)
            status.text = getString(R.string.detail_transaksi_arg,getString(R.string.status_bayar_args,data.transaksi.statusBayar))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}