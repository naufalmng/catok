package org.d3ifcool.catok.ui.beranda.transaksi

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.catok.core.data.repository.AppRepository
import org.d3ifcool.catok.core.data.source.local.entities.*
import org.d3ifcool.catok.core.data.source.local.model.DataPembayaran

@Suppress("UNCHECKED_CAST")
class TransaksiViewModel(private val repo: AppRepository) : ViewModel() {

    val getDataReturTransaksi: LiveData<ArrayList<ReturEntity>> =
        repo.getReturEntity() as LiveData<ArrayList<ReturEntity>>
    var isNavReady = MutableLiveData(true)
    var dataPembayaran = MutableLiveData<MutableList<DataPembayaran>>(null)
    var tempDataPembayaran = mutableListOf<DataPembayaran>()
    var isLinearLayoutManager = true
    var isSuccess = MutableLiveData(false)
    private val dataGrafik = repo.getListDataGrafik()

    val getDataHistoriTransaksi: LiveData<ArrayList<HistoriTransaksiEntity>> =
        repo.getArrayListDataHistoriTransaksi() as LiveData<ArrayList<HistoriTransaksiEntity>>

    fun getDataHistoriTransaksiByStatusBayar(): LiveData<ArrayList<HistoriTransaksiEntity>> =
        repo.getArrayListDataHistoriTransaksiByStatusBayar() as LiveData<ArrayList<HistoriTransaksiEntity>>

    val getDataProduk: LiveData<ArrayList<ProdukEntity>> =
        repo.getArrayListDataProduk() as LiveData<ArrayList<ProdukEntity>>
    val getDataTransaksi: LiveData<ArrayList<TransaksiEntity>> =
        repo.getDataTransaksi() as LiveData<ArrayList<TransaksiEntity>>
    val getDataTransaksiProduk: LiveData<ArrayList<TransaksiProdukEntity>> =
        repo.getListDataTransaksiProduk() as LiveData<ArrayList<TransaksiProdukEntity>>
    val getDataGrafik: LiveData<ArrayList<GrafikEntity>> =
        repo.getDataGrafik() as LiveData<ArrayList<GrafikEntity>>
    var listTransaksi = MutableLiveData<List<TransaksiEntity>>()

    val isDataTransaksiEmpty = MutableLiveData(true)

    fun searchProduk(query: String): LiveData<List<ProdukEntity>> {
        return repo.searchProduk(query).asLiveData()
    }

    fun updateDataGrafik(totalTransaksi: Double, tanggal: String) {
        Log.d("TransaksiViewModel", "updateDataGrafik: Masuk")
        Log.d("TransaksiViewModel", "updateDataGrafik: ${dataGrafik.size}")
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateDataGrafik(totalTransaksi, tanggal)
        }
    }

    fun returTransaksi(id: Int, qty: Int) {
        viewModelScope.launch {
            repo.returTransaksi(id, qty)
        }
    }

    fun insertDataGrafik(grafikEntity: GrafikEntity) {
        Log.d("TransaksiViewModel", "insertDataGrafik: Masuk")
        Log.d("TransaksiViewModel", "insertDataGrafik: ${dataGrafik.size}")
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertDataGrafik(grafikEntity)
        }
    }

    fun insertTransaksi(
        transaksi: TransaksiEntity,
        historiTransaksi: HistoriTransaksiEntity? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertTransaksi(transaksi)
            if (historiTransaksi != null) {
                repo.insertHistoriTransaksi(historiTransaksi)
            }
        }
    }

    @JvmName("getListIdTransaksi1")
    fun getListIdTransaksi() {
        listTransaksi.value = repo.getListTransaksi()
        Log.d("TransaksiViewModel", "getListIdTransaksi: $listTransaksi")
    }

    fun insertHistoriTransaksi(historiTransaksi: HistoriTransaksiEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertHistoriTransaksi(historiTransaksi)
            } catch (e: Exception) {
                Log.d("InsertTransaksi", e.toString().ifBlank { "Insert transaksi gagal" })
            }
        }
    }

    fun insertTransaksiProdukRecord(transaksiProdukEntity: TransaksiProdukEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertTransaksiProduk(transaksiProdukEntity)
        }
    }

    fun insertTransaksiProduk() {
        Log.d("TransaksiViewModel", "insertTransaksiProduk: ListTransaksi = $listTransaksi")
        val listQtyProduk = mutableListOf<Int>()
        fun getListIdProduk(): List<Int> {
            val listIdProduk = mutableListOf<Int>()
            for (i in tempDataPembayaran) {
                listIdProduk.add(i.nomor)
                listQtyProduk.add(i.qty)
            }
            return listIdProduk.ifEmpty { mutableListOf() }
        }

        val listIdProduk = getListIdProduk()
        Log.d("TransaksiViewModel", "insertTransaksiProduk: listIdProduk = $listIdProduk")
        Log.d("TransaksiViewModel", "insertTransaksiProduk: listQtyProduk = $listQtyProduk")
        fun getIdTransaksi(): String? {
            var id = ""
            for (i in listTransaksi.value!!) {
                id = i.id_transaksi
            }
            return if (id != "") id else null
        }

        val idTransaksi = getIdTransaksi()
        Log.d("TransaksiViewModel", "insertTransaksiProduk: IdTransaksi = $idTransaksi")
        fun getListTransaksiProduk(): List<TransaksiProdukEntity> {
            val listTransaksiProduk = mutableListOf<TransaksiProdukEntity>()
            val listIdsProduk = mutableListOf<Int>()
            val listQtysProduk = mutableListOf<Int>()

            if (listIdProduk.isNotEmpty() && idTransaksi != "" && listQtyProduk.isNotEmpty()) {
                try {
                    for (i in listIdProduk.indices) {
                        listIdsProduk.add(listIdProduk[i])
                        listQtysProduk.add(listQtyProduk[i])
                        listTransaksiProduk.add(
                            TransaksiProdukEntity(
                                id_produk = listIdProduk[i],
                                id_transaksi = idTransaksi!!,
                                qty = listQtyProduk[i]
                            )
                        )
                    }
                } catch (e: Exception) {
                    Log.d("TransaksiViewModel", "getListTransaksiProduk: ${e.message}")
                } finally {
                    viewModelScope.launch {
                        repo.insertReturData(
                            ReturEntity(
                                idProduk = listIdsProduk,
                                idTransaksi = idTransaksi!!,
                                qty = listQtysProduk
                            )
                        )
                    }
                }
            }
            Log.d(
                "TransaksiViewModel",
                "insertTransaksiProduk: listTransaksiProduk = $listTransaksiProduk"
            )
            return listTransaksiProduk.ifEmpty { mutableListOf() }
        }

        val listTransaksiProduk = getListTransaksiProduk()

        if (listTransaksiProduk.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    for (i in listTransaksiProduk.indices) {
                        repo.insertTransaksiProduk(listTransaksiProduk[i])
                        repo.updateStok(listIdProduk[i], listQtyProduk[i])
                    }
                    isSuccess.postValue(true)
                } catch (e: Exception) {
                    Log.d(
                        "InsertTransaksiProduk",
                        e.toString().ifBlank { "Insert Transaksi Produk Gagal" })
                }
            }
        }

    }

    fun deleteGrafikDataById(invoice: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteGrafikDataById(invoice)
        }
    }

    fun deleteTransaksiById(idTransaksi: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteTransaksiById(idTransaksi)
        }
    }

    fun deleteTransaksiProdukById(idHistori: String) {
        viewModelScope.launch {
            repo.deleteTransaksiProdukById(idHistori)
        }
    }

    fun updateHistoriTransaksiByInvoice(idHistori: String) {
        viewModelScope.launch {
            repo.updateHistoriTransaksiByInvoice(idHistori)
        }
    }
}
