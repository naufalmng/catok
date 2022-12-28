package org.d3ifcool.catok.ui.beranda.produk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3ifcool.catok.core.data.repository.AppRepository
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity

@Suppress("UNCHECKED_CAST")
class DataProdukViewModel(private val repo: AppRepository): ViewModel() {
    var mLastClickTime = 0L
    val isAllItemSelected = MutableLiveData(false)
    val tempProdukEntity = MutableLiveData<ProdukEntity>(null)
    var isLinearLayoutManager = true

    val getDataProduk : LiveData<ArrayList<ProdukEntity>> = repo.getArrayListDataProduk() as LiveData<ArrayList<ProdukEntity>>
    val isDataProdukEmpty = MutableLiveData<Boolean>(true)



    fun insertData(namaProduk: String, deskripsi: String, hargaBeli: Double, hargaJual: Double,satuan: Int,satuanPer: String, stok: Int,tanggal: String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertData(ProdukEntity(namaProduk = namaProduk, deskripsi = deskripsi, modal = hargaBeli, hargaJual = hargaJual,satuan = satuan,satuanPer = satuanPer, stok = stok, tanggal = tanggal))
        }
    }
    fun insertRecordProduk(produkEntity: ProdukEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertData(produkEntity)
        }
    }

    fun deleteData(ids: List<Int>) {
        val newIds = ids.toList()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.deleteData(newIds)
            }
        }
    }

    fun updateData(produkEntity: ProdukEntity) {
        viewModelScope.launch(Dispatchers.IO){
            repo.updateData(produkEntity)
        }
    }
}