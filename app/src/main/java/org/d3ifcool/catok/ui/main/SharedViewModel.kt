package org.d3ifcool.catok.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.catok.core.data.source.local.model.DataPembayaran
import org.d3ifcool.catok.core.data.source.local.model.Produk
import java.math.BigDecimal

class SharedViewModel: ViewModel() {
    var catatan = MutableLiveData<String>("")
    var jumlahKembalian = MutableLiveData<Double>(0.0)
    val fotoToko = MutableLiveData<Bitmap>()
    val getFotoToko : LiveData<Bitmap> = fotoToko
    var namaToko = MutableLiveData<String?>()
    val getNamaToko : LiveData<String?> = namaToko
    var totalTransaksi = MutableLiveData(0.0)

    var jenisPembayaran = MutableLiveData<String>("")
    var totalDenganDiskon = MutableLiveData<Double>(0.0)
    var hasilAkhir: Double = 0.0
    var _tempDataPembayaran = MutableLiveData<MutableList<DataPembayaran>>(mutableListOf())
    val tempDataPembayaran : LiveData<MutableList<DataPembayaran>> get() = (_tempDataPembayaran)
    var _jumlahDiskon = MutableLiveData<Double>(0.0)
    val jumlahDiskon : LiveData<Double> get() = (_jumlahDiskon)
    var _subTotal = MutableLiveData(0.0)
    var totalAkhir = MutableLiveData<Double>(0.0)
    var jumlahBayar = MutableLiveData(0.0)
    val hasilDiskon = MutableLiveData<BigDecimal>()
    var tempDataProduk = MutableLiveData<Produk?>()

    var tempQty = MutableLiveData<Int>(0)
    var isSearchViewVisible = MutableLiveData<Boolean>(true)

    var itemPos = MutableLiveData<Int>()
    var qty = MutableLiveData<Int>(0)
//    val dataPembayaran: LiveData<MutableList<DataPembayaran>> get() = (tempDataPembayaran)
    val _isDialogPembayaran = MutableLiveData<Boolean>(false)
    val isDialogPembayaran: LiveData<Boolean> = _isDialogPembayaran
    val isBackPressed = MutableLiveData<Int>(0)

    fun getProdukIdList(): List<Int>{
        lateinit var produkIdList: ArrayList<Int>
        for (i in tempDataProduk.value!!.produkIdList!!.indices){
            produkIdList.add(tempDataProduk.value!!.produkIdList?.get(i)!!)
        }
        return produkIdList
    }
    fun getProdukNameList(): List<String>{
        lateinit var list: ArrayList<String>
        for (i in tempDataProduk.value!!.produkIdList!!.indices){
            list.add(tempDataProduk.value!!.produkNameList?.get(i)!!)
        }
        return list
    }
    fun getProdukQtyList(): List<Int>{
        lateinit var list: ArrayList<Int>
        for (i in tempDataProduk.value!!.produkIdList!!.indices){
            list.add(tempDataProduk.value!!.produkQtyList?.get(i)!!)
        }
        return list
    }
    fun getProdukSatuanPerList(): List<String>{
        lateinit var list: ArrayList<String>
        for (i in tempDataProduk.value!!.produkIdList!!.indices){
            list.add(tempDataProduk.value!!.produkSatuanPerList?.get(i)!!)
        }
        return list
    }
    fun getProdukPriceList(): List<Double>{
        lateinit var list: ArrayList<Double>
        for (i in tempDataProduk.value!!.produkIdList!!.indices){
            list.add(tempDataProduk.value!!.hargaProdukList?.get(i)!!)
        }
        return list
    }

}