package org.d3ifcool.catok.ui.pengaturan

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.catok.core.data.repository.AppRepository
import org.d3ifcool.catok.core.data.source.local.entities.HistoriTransaksiEntity
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.core.data.source.local.entities.ProfilEntity

class PengaturanViewModel(private val repo: AppRepository): ViewModel() {
    var isBtnBackupDataClicked: Boolean? = null
    var dataProfil = MutableLiveData(getProfil())
    val getDataProfil : LiveData<ProfilEntity> = dataProfil
    var tempTransaksi = MutableLiveData<ArrayList<HistoriTransaksiEntity>>()
    var tempProduk = MutableLiveData<ArrayList<ProdukEntity>>()

    fun insertProfil(profil: ProfilEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertProfil(profil)
        }
    }
    fun updateProfil(nama: String, gambar: Bitmap){
        viewModelScope.launch(Dispatchers.IO){
            repo.updateProfil(nama,gambar)
        }
    }

    private fun getProfil(): ProfilEntity{
        Log.d("PengaturanViewModel", "getProfil: ${repo.getProfil()}")
        return repo.getProfil()
    }
}