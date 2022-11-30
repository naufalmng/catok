package org.d3ifcool.catok.ui.beranda.transaksi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.catok.core.data.repository.AppRepository
import org.d3ifcool.catok.core.data.source.local.entities.*
import org.d3ifcool.catok.core.data.source.local.model.DataPembayaran
import org.d3ifcool.catok.core.data.source.local.model.Produk
import java.lang.Exception

@Suppress("UNCHECKED_CAST")
class TransaksiViewModel(private val repo: AppRepository): ViewModel() {
    var dataPembayaran = MutableLiveData<MutableList<DataPembayaran>>(null)
    var tempDataPembayaran = mutableListOf<DataPembayaran>()
//    var tempProdukQtyList= MutableLiveData<MutableList<Int?>>(mutableListOf(null))
//    var listHargaProduk = MutableLiveData<MutableList<Double?>>(mutableListOf(null))
    var mLastClickTime = 0L
    val isAllItemSelected = MutableLiveData(false)
    val tempProdukEntity = MutableLiveData<HistoriTransaksiEntity>(null)
    var isLinearLayoutManager = true

    val getDataHistoriTransaksi : LiveData<ArrayList<HistoriTransaksiEntity>> = repo.getArrayListDataTransaksi() as LiveData<ArrayList<HistoriTransaksiEntity>>
    val getDataProduk : LiveData<ArrayList<ProdukEntity>> = repo.getArrayListDataProduk() as LiveData<ArrayList<ProdukEntity>>

    val isDataTransaksiEmpty = MutableLiveData<Boolean>(true)
    fun insertTransaksi(transaksi: TransaksiEntity){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertTransaksi(transaksi)

            }catch (e: Exception){
                Log.d("UpdateStok", e.toString().ifBlank { "Transaksi Gagal" })
            }
        }
    }
    fun insertTransaksiProduk(transaksiProduk: TransaksiProdukEntity){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertTransaksiProduk(transaksiProduk)

            }catch (e: Exception){
                Log.d("UpdateStok", e.toString().ifBlank { "Transaksi Gagal" })
            }
        }
    }

    fun setupDataPembayaran() {
        if(tempDataPembayaran.isNotEmpty()){
            dataPembayaran.value = tempDataPembayaran
        }
    }
}