package org.d3ifcool.catok.ui.beranda.produk.insert

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.DialogFragment
import org.d3ifcool.catok.databinding.DialogInsertProdukBinding
import org.d3ifcool.catok.ui.beranda.produk.DataProdukViewModel
import org.d3ifcool.catok.utils.enableOnClickAnimation
import java.text.SimpleDateFormat
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class InsertProdukDialog : DialogFragment() {

    private var _binding: DialogInsertProdukBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DataProdukViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogInsertProdukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()


//        val layoutParams1 = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
//        )
//        layoutParams1.setMargins(20, 60, 20, 120)
//        val layoutParams2 = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
//        )
//        layoutParams1.setMargins(20, 60, 20, 30)
//        if(binding.llBarcode.visibility == View.GONE) binding.llBarcode.layoutParams = layoutParams1
//        else binding.llBarcode.layoutParams = layoutParams2
        setupDate()
    }

    private fun insertProduk() {
        with(binding){
            if(TextUtils.isEmpty(etNamaProduk.text.toString())){
                Toast.makeText(requireContext(), "Nama Produk Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show()
                return
            }
            if(TextUtils.isEmpty(etDeskripsi.text.toString())){
                Toast.makeText(requireContext(), "Deskripsi Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show()
                return
            }
            if(TextUtils.isEmpty(etHargaBeli.text.toString())){
                Toast.makeText(requireContext(), "Harga Beli Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show()
                return
            }
            if(TextUtils.isEmpty(etHargaJual.text.toString())){
                Toast.makeText(requireContext(), "Harga Jual Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show()
                return
            }
            if(TextUtils.isEmpty(etSatuan.text.toString())){
                Toast.makeText(requireContext(), "Satuan Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show()
                return
            }
            if(TextUtils.isEmpty(etStokAwal.text.toString())){
                Toast.makeText(requireContext(), "Stok Awal Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show()
                return
            }
            viewModel.insertData(namaProduk = etNamaProduk.text.toString(), deskripsi = etDeskripsi.text.toString(),etHargaBeli.text.toString().toDouble(), hargaJual = etHargaJual.text.toString().toDouble(), satuan = etSatuan.text.toString().toInt(), stok = etStokAwal.text.toString().toInt(), tanggal = tanggal.text.toString())
            Toast.makeText(requireContext(), "Produk berhasil ditambah !", Toast.LENGTH_SHORT)
                .show()
            dismiss()
        }
    }

    private fun setupListeners() {
        with(binding){
            btnBatal.enableOnClickAnimation()
            btnSimpan.enableOnClickAnimation()
            close.enableOnClickAnimation()
            close.setOnClickListener{
                dismiss()
            }
            btnSimpan.setOnClickListener{
                insertProduk()

            }
            btnBatal.setOnClickListener{
                dismiss()
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
//        params.width = LinearLayoutCompat.LayoutParams.MATCH_PARENT
//        params.height = LinearLayoutCompat.LayoutParams.MATCH_PARENT
//        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
//    }

    @SuppressLint("SimpleDateFormat")
    private fun setupDate() {
        val currentDate = Calendar.getInstance().time
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id","ID"))
        binding.tanggal.setText(sdf.format(currentDate))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}