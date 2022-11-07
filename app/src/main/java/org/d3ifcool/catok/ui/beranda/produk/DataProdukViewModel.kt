package org.d3ifcool.catok.ui.beranda.produk

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3ifcool.catok.core.data.repository.AppRepository
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.utils.State
@Suppress("UNCHECKED_CAST")
class DataProdukViewModel(private val repo: AppRepository): ViewModel() {
    var mLastClickTime = 0L
    val isAllItemSelected = MutableLiveData(false)
    val tempProdukEntity = MutableLiveData<ProdukEntity>(null)
    var isLinearLayoutManager = true
    private var _state = MutableLiveData<State>()
    private val state : LiveData<State> = _state

    val getDataProduk : LiveData<ArrayList<ProdukEntity>> = repo.getArrayListDataProduk() as LiveData<ArrayList<ProdukEntity>>
    val isDataProdukEmpty: LiveData<Boolean> = repo.isDataProdukEmpty()



    fun insertData(namaProduk: String, deskripsi: String, hargaBeli: Double, hargaJual: Double,satuan: Int, stok: Int,tanggal: String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertData(ProdukEntity(namaProduk = namaProduk, deskripsi = deskripsi, hargaBeli = hargaBeli, hargaJual = hargaJual,satuan = satuan, stok = stok, tanggal = tanggal))
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