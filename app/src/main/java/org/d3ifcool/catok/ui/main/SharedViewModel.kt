@file:Suppress("PropertyName")

package org.d3ifcool.catok.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.catok.core.data.source.local.model.DataPembayaran
import org.d3ifcool.catok.core.data.source.local.model.Produk
import java.math.BigDecimal

class SharedViewModel: ViewModel() {
    var catatan = MutableLiveData("")
    var jumlahKembalian = MutableLiveData(0.0)
    val fotoToko = MutableLiveData<Bitmap>()
    val getFotoToko : LiveData<Bitmap> = fotoToko
    var namaToko = MutableLiveData<String?>()
    val getNamaToko : LiveData<String?> = namaToko
    var totalTransaksi = MutableLiveData(0.0)

    var jenisPembayaran = MutableLiveData("")
    var totalDenganDiskon = MutableLiveData(0.0)
    var _tempDataPembayaran = MutableLiveData<MutableList<DataPembayaran>>(mutableListOf())
    val tempDataPembayaran : LiveData<MutableList<DataPembayaran>> get() = (_tempDataPembayaran)
    var _jumlahDiskon = MutableLiveData(0.0)
    val jumlahDiskon : LiveData<Double> get() = (_jumlahDiskon)
    var _subTotal = MutableLiveData(0.0)
    var jumlahBayar = MutableLiveData(0.0)
    val hasilDiskon = MutableLiveData<BigDecimal>()
    var tempDataProduk = MutableLiveData<Produk?>()

    var isSearchViewVisible = MutableLiveData(true)

    var qty = MutableLiveData(0)
    val _isDialogPembayaran = MutableLiveData(false)
    val isBackPressed = MutableLiveData(0)

}