package org.d3ifcool.catok.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.catok.core.data.source.local.model.DataPembayaran
import org.d3ifcool.catok.core.data.source.local.model.Produk

class SharedViewModel: ViewModel() {
    var _tempDataPembayaran = MutableLiveData<MutableList<DataPembayaran>>(mutableListOf())
    val tempDataPembayaran : LiveData<MutableList<DataPembayaran>> get() = (_tempDataPembayaran)
    var tempDataProduk = MutableLiveData<Produk?>()
    var tempQty = MutableLiveData<Int>(0)

    var itemPos = MutableLiveData<Int>()
    var qty = MutableLiveData<Int>(0)
//    val dataPembayaran: LiveData<MutableList<DataPembayaran>> get() = (tempDataPembayaran)
    val _isDialogPembayaran = MutableLiveData<Boolean>(false)
    val isDialogPembayaran: LiveData<Boolean> = _isDialogPembayaran
    val isBackPressed = MutableLiveData<Int>(0)
}