package org.d3ifcool.catok.ui.beranda.transaksi

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.catok.core.data.repository.AppRepository
import org.d3ifcool.catok.core.data.source.local.entities.*
import org.d3ifcool.catok.core.data.source.local.model.DataPembayaran

@Suppress("UNCHECKED_CAST")
class TransaksiViewModel(private val repo: AppRepository): ViewModel() {

    var isNavReady = MutableLiveData(true)
    var dataPembayaran = MutableLiveData<MutableList<DataPembayaran>>(null)
    var tempDataPembayaran = mutableListOf<DataPembayaran>()
    var isLinearLayoutManager = true
    var isSuccess = MutableLiveData<Boolean>(false)
    val dataGrafik = repo.getListDataGrafik()

    val getDataHistoriTransaksi : LiveData<ArrayList<HistoriTransaksiEntity>> = repo.getArrayListDataHistoriTransaksi() as LiveData<ArrayList<HistoriTransaksiEntity>>
    val getDataProduk : LiveData<ArrayList<ProdukEntity>> = repo.getArrayListDataProduk() as LiveData<ArrayList<ProdukEntity>>
    val getDataTransaksi : LiveData<ArrayList<TransaksiEntity>> = repo.getDataTransaksi() as LiveData<ArrayList<TransaksiEntity>>
    val getDataTransaksiProduk : LiveData<ArrayList<TransaksiProdukEntity>> = repo.getListDataTransaksiProduk() as LiveData<ArrayList<TransaksiProdukEntity>>
    val getDataGrafik: LiveData<ArrayList<GrafikEntity>> = repo.getDataGrafik() as LiveData<ArrayList<GrafikEntity>>
    var listTransaksi = MutableLiveData<List<TransaksiEntity>>()

    val isDataTransaksiEmpty = MutableLiveData<Boolean>(true)

    fun searchProduk(query: String): LiveData<List<ProdukEntity>> {
        return repo.searchProduk(query).asLiveData()
    }

    fun insertDataGrafik(grafikEntity: GrafikEntity){
        Log.d("TransaksiViewModel", "insertDataGrafik: Masuk")
        Log.d("TransaksiViewModel", "insertDataGrafik: ${dataGrafik.size}")
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertDataGrafik(grafikEntity)
        }
//        if(dataGrafik.isEmpty()){
//            viewModelScope.launch(Dispatchers.IO) {
//                repo.insertDataGrafik(grafikEntity)
//            }
//        } else{
//            for(i in dataGrafik.indices){
//                Log.d("TransaksiViewModel", "insertDataGrafik: ${dataGrafik[i].bulan}")
//                if(dataGrafik[i].bulan == getCurrentMonth()){
//                    viewModelScope.launch(Dispatchers.IO) {
//                        repo.updateDataGrafik(dataGrafik[i].id, grafikEntity.totalTransaksi)
//                    }
//                }else{
//                    viewModelScope.launch(Dispatchers.IO) {
//                        repo.insertDataGrafik(grafikEntity)
//                    }
//                }
//            }
//        }

    }

    fun insertTransaksi(transaksi: TransaksiEntity, historiTransaksi: HistoriTransaksiEntity? = null){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertTransaksi(transaksi)
            if(historiTransaksi!=null){
                repo.insertHistoriTransaksi(historiTransaksi)
            }
        }

//        Log.d("TransaksiViewModel", "insertTransaksi: ${repo.getListTransaksi()}")
    }

    @JvmName("getListIdTransaksi1")
    fun getListIdTransaksi(){
        listTransaksi.value = repo.getListTransaksi()
        Log.d("TransaksiViewModel", "getListIdTransaksi: ${listTransaksi}")
    }

    fun insertHistoriTransaksi(historiTransaksi: HistoriTransaksiEntity){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertHistoriTransaksi(historiTransaksi)
            }catch (e: Exception){
                Log.d("InsertTransaksi", e.toString().ifBlank { "Insert transaksi gagal" })
            }
        }
    }
    fun insertTransaksiProdukRecord(transaksiProdukEntity: TransaksiProdukEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertTransaksiProduk(transaksiProdukEntity)
        }
    }
    fun insertTransaksiProduk(){
        Log.d("TransaksiViewModel", "insertTransaksiProduk: ListTransaksi = $listTransaksi")
        var listQtyProduk = mutableListOf<Int>()
        fun getListIdProduk(): List<Int>{
            var listIdProduk = mutableListOf<Int>()
            for (i in tempDataPembayaran){
                listIdProduk.add(i.nomor)
                listQtyProduk.add(i.qty)
            }
            return if (listIdProduk.isNotEmpty()) listIdProduk
            else mutableListOf()
        }
        var listIdProduk = getListIdProduk()
        Log.d("TransaksiViewModel", "insertTransaksiProduk: listIdProduk = $listIdProduk")
        Log.d("TransaksiViewModel", "insertTransaksiProduk: listQtyProduk = $listQtyProduk")
        fun getIdTransaksi(): Int?{
            var id = -1
            for(i in listTransaksi.value!!){
                id=i.id_transaksi
            }
            return if(id!=-1) id else null
        }
        var idTransaksi = getIdTransaksi()
        Log.d("TransaksiViewModel", "insertTransaksiProduk: IdTransaksi = $idTransaksi")
        fun getListTransaksiProduk(): List<TransaksiProdukEntity>{
            var listTransaksiProduk = mutableListOf<TransaksiProdukEntity>()

            if(listIdProduk.isNotEmpty() && idTransaksi!=-1 && listQtyProduk.isNotEmpty()){
                for (i in listIdProduk.indices){
                    listTransaksiProduk.add(TransaksiProdukEntity(id_produk = listIdProduk[i], id_transaksi = idTransaksi!!, qty = listQtyProduk[i] ))
                }
            }
            Log.d("TransaksiViewModel", "insertTransaksiProduk: listTransaksiProduk = $listTransaksiProduk")
            return if (listTransaksiProduk.isNotEmpty()) listTransaksiProduk
            else mutableListOf()
        }
        var listTransaksiProduk = getListTransaksiProduk()

        if(listTransaksiProduk.isNotEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    for (i in listTransaksiProduk.indices){
                        repo.insertTransaksiProduk(listTransaksiProduk[i])
                        repo.updateStok(listIdProduk[i],listQtyProduk[i])
                    }
                    isSuccess.postValue(true)
                }catch (e: Exception){
                    Log.d("InsertTransaksiProduk", e.toString().ifBlank { "Insert Transaksi Produk Gagal" })
                }
            }
        }

    }
    fun clearGraphicData() {
        viewModelScope.launch {
            repo.deleteDataGrafik()
        }
    }

    fun setupDataPembayaran() {
        if(tempDataPembayaran.isNotEmpty()){
            dataPembayaran.value = tempDataPembayaran
        }
    }

    fun deleteTransaksi() {
        viewModelScope.launch {
            repo.deleteTransaksi()
            Log.d("TransaksiViewModel", "deleteTransaksi: ${repo.getListTransaksi()}")
        }
    }

    fun deleteHistoriTransaksi() {
        viewModelScope.launch {
            repo.deleteHistoriTransaksi()
            Log.d("TransaksiViewModel", "deleteTransaksi: ${dataGrafik}")
        }
    }
    fun deleteTransaksiProduk() {
        viewModelScope.launch {
            repo.deleteTransaksiProduk()
            Log.d("TransaksiViewModel", "deleteTransaksiProduk: ${repo.getListTransaksi()}")
        }
    }
}