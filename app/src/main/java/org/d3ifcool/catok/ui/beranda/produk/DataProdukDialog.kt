package org.d3ifcool.catok.ui.beranda.produk

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import org.d3ifcool.catok.core.data.DataStorePreferences
import org.d3ifcool.catok.core.data.dataStore
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.databinding.DialogDataProdukBinding
import org.d3ifcool.catok.utils.enableOnClickAnimation
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class DataProdukDialog : DialogFragment() {

    private var _binding: DialogDataProdukBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DataProdukViewModel by viewModel()
    private val args: DataProdukDialogArgs by navArgs()
    private var satuanPerValue = ""
    private val dataStorePreferences: DataStorePreferences by lazy {
        DataStorePreferences(requireActivity().dataStore)
    }
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
        with(binding) {
            tvTambahProduk.visibility = if (args.isInsert) View.VISIBLE else View.GONE
            tvEditProduk.visibility = if (args.isInsert) View.GONE else View.VISIBLE
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupTextWatcher()
        if (args.produk != null) {
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
//        else binding.llBarcode.layoutParams = layoutParams
    }

    private fun setupTextWatcher() {
        with(binding) {
//            etModal.addCurrencyFormatter()
//            etHargaNJual.addCurrencyFormatter()

//            etModal.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//                }
//
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//                }
//
//                override fun afterTextChanged(s: Editable?) {
//                    if (!s.toString().startsWith("Rp. ")) {
//                        etModal.setText("Rp. ")
//                        Selection.setSelection(etModal.text, etModal.text.toString().length)
//                    }
////                    if(s.toString().startsWith("Rp. ") && !etModal.hasFocus()){
////                        Toast.makeText(requireContext(), etModal.text.toString(), Toast.LENGTH_SHORT).show()
////
//////
//////                        etModal.setTextColor(ContextCompat.getColor(requireContext(),R.color.abu80)) else
//////                            etModal.setTextColor(ContextCompat.getColor(requireContext(),R.color.orange))
////                    }
//
//                }
//            })
//            etModal.setOnFocusChangeListener { p0, _ ->
//                if (p0?.hasFocus() == false && etModal.text.toString() == "Rp. ") {
//                    etModal.setTextColor(ContextCompat.getColor(requireContext(),R.color.abu80))
//                }else etModal.setTextColor(ContextCompat.getColor(requireContext(),R.color.orange))
//            }
        }
    }

    private fun updateUi() {
        val item = args.produk!!
        val spinnerValue = when(args.produk!!.satuanPer){
            "Pcs" -> 0
            "Kg" -> 1
            "Ton" -> 2
            "Ons" -> 3
            "Dus" -> 4
            "Bungkus" -> 5
            "Butir" -> 6
            "Liter" -> 7
            else -> -1
        }
        with(binding) {
            etNamaProduk.setText(item.namaProduk)
            etDeskripsi.setText(item.deskripsi)
            etModal.setText(item.modal.toInt().toString())
            etHargaJual.setText(item.hargaJual.toInt().toString())
            etSatuan.setText(item.satuan.toString())
            etStokAwal.setText(item.stok.toString())
            spinnerSatuanPer.selectItemByIndex(spinnerValue)

        }
    }

    private fun setupProduk() {
        val item: ProdukEntity?
        if (args.produk != null) item = args.produk!! else item = null

        with(binding) {
            if (TextUtils.isEmpty(etNamaProduk.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    "Nama Produk Tidak Boleh Kosong !",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (TextUtils.isEmpty(etDeskripsi.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    "Deskripsi Tidak Boleh Kosong !",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (TextUtils.isEmpty(etModal.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    "Modal Tidak Boleh Kosong !",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (TextUtils.isEmpty(etHargaJual.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    "Harga Jual Tidak Boleh Kosong !",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (TextUtils.isEmpty(etSatuan.text.toString())) {
                Toast.makeText(requireContext(), "Satuan Tidak Boleh Kosong !", Toast.LENGTH_SHORT)
                    .show()
                return
            }
            if (spinnerSatuanPer.selectedIndex<0 && satuanPerValue=="") {
                Toast.makeText(requireContext(), "Satuan Per Tidak Boleh Kosong !", Toast.LENGTH_SHORT)
                    .show()
                return
            }
            if (TextUtils.isEmpty(etStokAwal.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    "Stok Awal Tidak Boleh Kosong !",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            if (args.isInsert){
                viewModel.insertData(
                    namaProduk = etNamaProduk.text.toString(),
                    deskripsi = etDeskripsi.text.toString(),
                    etModal.getNumericValue(),
                    hargaJual = etHargaJual.getNumericValue(),
                    satuan = etSatuan.text.toString().toInt(),
                    satuanPer = satuanPerValue,
                    stok = etStokAwal.text.toString().toInt(),
                    tanggal = getCurrentDate()
                )
                lifecycleScope.launch {
                    dataStorePreferences.saveDataUpdateSetting(requireContext(),true)
                }
            }
            else {
                if (item != null) {
                    viewModel.updateData(
                        ProdukEntity(
                            item.id_produk,
                            item.barcode,
                            etNamaProduk.text.toString(),
                            etDeskripsi.text.toString(),
                            etModal.getNumericValue(),
                            etHargaJual.getNumericValue(),
                            etSatuan.text.toString().toInt(),
                            satuanPerValue,
                            etStokAwal.text.toString().toInt(),
                            getCurrentDate()
                        )
                    )
                }
            }

            Toast.makeText(
                requireContext(), if (!args.isInsert) "Berhasil menyunting produk !" else "Produk berhasil ditambah !", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    private fun setupListeners() {
        with(binding) {
            btnBatal.enableOnClickAnimation()
            btnSimpan.enableOnClickAnimation()
            btnSimpan.setOnClickListener {
                setupProduk()
            }
            btnBatal.setOnClickListener {
                dismiss()
            }
            spinnerSatuanPer.setOnSpinnerItemSelectedListener<String>{ oldIndex, oldItem, newIndex, text ->
                satuanPerValue = text
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

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
        return sdf.format(currentDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        satuanPerValue = ""
    }
}