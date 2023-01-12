package org.d3ifcool.catok.ui.beranda.produk

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
import org.d3ifcool.catok.R
import org.d3ifcool.catok.core.data.DataStorePreferences
import org.d3ifcool.catok.core.data.dataStore
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.databinding.DialogDataProdukBinding
import org.d3ifcool.catok.utils.enableOnClickAnimation
import org.d3ifcool.catok.utils.getCurrentDate2
import org.koin.androidx.viewmodel.ext.android.viewModel


class DataProdukDialog : DialogFragment() {

    private var _binding: DialogDataProdukBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DataProdukViewModel by viewModel()
    private val args: DataProdukDialogArgs by navArgs()
    private var satuanPerValue = ""
    private val dataStorePreferences: DataStorePreferences by lazy {
        DataStorePreferences(requireActivity().dataStore)
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
        binding.spinnerSatuanPer.selectItemByIndex(0)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        if (args.produk != null) {
            updateUi()
        }
    }

    private fun updateUi() {
        val item = args.produk!!
        val spinnerValue = when(args.produk!!.satuanPer){
            resources.getStringArray(R.array.jenis_satuan)[0] -> 0
            resources.getStringArray(R.array.jenis_satuan)[1] -> 1
            resources.getStringArray(R.array.jenis_satuan)[2] -> 2
            resources.getStringArray(R.array.jenis_satuan)[3] -> 3
            resources.getStringArray(R.array.jenis_satuan)[4] -> 4
            resources.getStringArray(R.array.jenis_satuan)[5] -> 5
            resources.getStringArray(R.array.jenis_satuan)[6] -> 6
            resources.getStringArray(R.array.jenis_satuan)[7] -> 7
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
        val item: ProdukEntity? = if (args.produk != null) args.produk!! else null

        with(binding) {
            if (TextUtils.isEmpty(etNamaProduk.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.produk_tidak_boleh_kosong),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (TextUtils.isEmpty(etDeskripsi.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.deskripsi_tidak_boleh_kosong),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (TextUtils.isEmpty(etModal.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.modal_tidak_boleh_kosong),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (TextUtils.isEmpty(etHargaJual.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.harga_jual_tidak_boleh_kosong),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (TextUtils.isEmpty(etSatuan.text.toString())) {
                Toast.makeText(requireContext(), getString(R.string.satuan_warning), Toast.LENGTH_SHORT)
                    .show()
                return
            }
            if (spinnerSatuanPer.selectedIndex<0 && satuanPerValue=="") {
                Toast.makeText(requireContext(), getString(R.string.satuan_per_warning), Toast.LENGTH_SHORT)
                    .show()
                return
            }
            if (TextUtils.isEmpty(etStokAwal.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.stok_warning),
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
                    satuanPer =  if(satuanPerValue!="") satuanPerValue else resources.getStringArray(R.array.jenis_satuan)[0],
                    stok = etStokAwal.text.toString().toInt(),
                    tanggal = getCurrentDate2()
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
                            if(satuanPerValue!="") satuanPerValue else resources.getStringArray(R.array.jenis_satuan)[0],
                            etStokAwal.text.toString().toInt(),
                            getCurrentDate2()
                        )
                    )
                }
            }

            Toast.makeText(
                requireContext(), if (!args.isInsert) getString(R.string.berhasil_menyunting_produk) else getString(R.string.produk_berhasil_ditambah), Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    private fun setupListeners() {
        with(binding) {
            btnBatalSimpan.enableOnClickAnimation()
            btnSimpan.enableOnClickAnimation()
            btnSimpan.setOnClickListener {
                setupProduk()
            }
            btnBatalSimpan.setOnClickListener {
                dismiss()
            }
            spinnerSatuanPer.setOnSpinnerItemSelectedListener<String>{ oldIndex, oldItem, newIndex, text ->
                satuanPerValue = text
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        satuanPerValue = ""
    }
}