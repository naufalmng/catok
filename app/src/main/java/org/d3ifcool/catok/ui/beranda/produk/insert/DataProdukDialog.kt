package org.d3ifcool.catok.ui.beranda.produk.insert

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.databinding.DialogDataProdukBinding
import org.d3ifcool.catok.ui.beranda.produk.DataProdukViewModel
import org.d3ifcool.catok.utils.enableOnClickAnimation
import java.text.SimpleDateFormat
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DataProdukDialog : DialogFragment() {

    private var _binding: DialogDataProdukBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DataProdukViewModel by viewModel()
    private val args: DataProdukDialogArgs by navArgs()

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
        dialog?.setCanceledOnTouchOutside(false)
        _binding = DialogDataProdukBinding.inflate(inflater, container, false)
        with(binding){
            tvTambahProduk.visibility = if(args.isInsert) View.VISIBLE else View.GONE
            tvEditProduk.visibility = if(args.isInsert) View.GONE else View.VISIBLE
        }
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        if(args.produk!=null){
            updateUi()
        }


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
    private fun updateUi() {
        val item = args.produk!!
        with(binding){
            etNamaProduk.setText(item.namaProduk)
            etDeskripsi.setText(item.deskripsi)
            etHargaBeli.setText(item.hargaBeli.toString())
            etHargaJual.setText(item.hargaJual.toString())
            etSatuan.setText(item.satuan.toString())
            etStokAwal.setText(item.stok.toString())
        }
    }

    private fun setupProduk() {
        var item: ProdukEntity?
        if(args.produk!=null) item = args.produk!! else item = null

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

            if(args.isInsert) viewModel.insertData(namaProduk = etNamaProduk.text.toString(), deskripsi = etDeskripsi.text.toString(),etHargaBeli.text.toString().toDouble(), hargaJual = etHargaJual.text.toString().toDouble(), satuan = etSatuan.text.toString().toInt(), stok = etStokAwal.text.toString().toInt(), tanggal = tanggal.text.toString())
            else {
                if(item!=null){
                    viewModel.updateData(ProdukEntity(item.id,item.barcode,etNamaProduk.text.toString(),etDeskripsi.text.toString(),etHargaBeli.text.toString().toDouble(),etHargaJual.text.toString().toDouble(),etSatuan.text.toString().toInt(),etStokAwal.text.toString().toInt(),tanggal.text.toString()))
                }
            }

            Toast.makeText(requireContext(), if(!args.isInsert) "Berhasil menyunting produk !" else "Produk berhasil ditambah !", Toast.LENGTH_SHORT)
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
                setupProduk()
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