package org.d3ifcool.catok.ui.beranda.transaksi

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.source.local.model.Produk
import org.d3ifcool.catok.databinding.FragmentDetailHistoriTransaksiBinding
import org.d3ifcool.catok.ui.grafik.GrafikViewModel
import org.d3ifcool.catok.utils.addUnderline
import org.d3ifcool.catok.utils.toRupiahFormat
import org.d3ifcool.catok.utils.toRupiahFormatV2
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception
import java.util.*


class DetailHistoriTransaksiFragment : Fragment() {

    private var _binding: FragmentDetailHistoriTransaksiBinding? = null
    private val binding get() = _binding!!
    private val transaksiViewModel: TransaksiViewModel by viewModel()
    private val grafikViewModel: GrafikViewModel by viewModel()
    val data: DetailHistoriTransaksiFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailHistoriTransaksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        updateUi()
    }

    private fun updateUi() {
        val i = data.transaksi
        with(binding){
            tanggal.text = getString(R.string.detail_transaksi_arg,data.transaksi.tanggal)
            invoice.text = getString(R.string.invoice_arg,data.transaksi.id_histori)
            subTotal.text = getString(R.string.detail_transaksi_arg,data.transaksi.total.toRupiahFormatV2().addUnderline())
            total.text = getString(R.string.detail_transaksi_arg,(data.transaksi.total-data.transaksi.diskon).toRupiahFormatV2().uppercase(Locale("id","ID")).addUnderline())

            diskon.text = getString(R.string.detail_transaksi_arg,data.transaksi.diskon.toRupiahFormatV2())
            bayar.text = getString(R.string.detail_transaksi_arg,data.transaksi.bayar.toRupiahFormatV2())
            kembalian.text = getString(R.string.detail_transaksi_arg,data.transaksi.kembalian.toRupiahFormatV2())
            pembayaran.text = getString(R.string.detail_transaksi_arg,data.transaksi.pembayaran.addUnderline())
            catatan.text = getString(R.string.detail_transaksi_arg,data.transaksi.catatan)
            produk.text = i.produkDibeli.replace("*","\n")
            status.text = getString(R.string.detail_transaksi_arg,getString(R.string.status_bayar_args,data.transaksi.statusBayar))
            btnKembali.setOnClickListener{
                findNavController().popBackStack(R.id.detailHistoriTransaksiFragment, true)
            }
            btnRetur.setOnClickListener{
                AlertDialog.Builder(requireContext()).apply {
                    setMessage(getString(R.string.retur_msg))
                    setPositiveButton(R.string.retur) { _, _ ->
                        returDataTransaksi()
                    }
                    setNegativeButton(R.string.batal) { dialog, _ ->
                        dialog.cancel()
                    }
                    show()
                }
            }
        }
    }

    private fun returDataTransaksi() {
        transaksiViewModel.getDataReturTransaksi.observe(viewLifecycleOwner){
            var isMatch = false
            var listIdProduk = listOf<Int>()
            var listQtyProduk = listOf<Int>()
            var idTransaksi = ""
            if(it!=null){
                try {
                    for (i in it){
                        if (i.idTransaksi == data.transaksi.id_histori){
                            isMatch = true
                            listIdProduk = i.idProduk
                            listQtyProduk = i.qty
                            idTransaksi = i.idTransaksi
                        }
                    }
                }catch (e: Exception){
                    Log.d("DetailHistoriTransaksi", "returDataTransaksi: ${e.message}")
                    return@observe
                }
                finally {
                    if(isMatch==true){
                        try {
                            for (i in listIdProduk.indices){
                                transaksiViewModel.returTransaksi(listIdProduk[i],listQtyProduk[i])
                            }
                        }catch (e: Exception){}
                        finally {
                            if(idTransaksi!=""){
                                transaksiViewModel.deleteHistoriTransaksiByInvoice(data.transaksi.id_histori)
                                transaksiViewModel.deleteTransaksiById(idTransaksi)
                                transaksiViewModel.deleteTransaksiProdukById(data.transaksi.id_histori)
                                transaksiViewModel.deleteGrafikDataById(data.transaksi.id_histori)
                                Toast.makeText(requireContext(), "Transaksi berhasil diretur", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Toast.makeText(requireContext(), "Transaksi ini tidak dapat diretur !", Toast.LENGTH_SHORT).show()
                    }
                }

                findNavController().popBackStack(R.id.detailHistoriTransaksiFragment, true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}