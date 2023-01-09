package org.d3ifcool.catok.ui.grafik
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.catok.core.data.repository.AppRepository
import org.d3ifcool.catok.core.data.source.local.entities.GrafikEntity
import kotlin.math.tan

class GrafikViewModel(val repo: AppRepository): ViewModel() {
    val dataGrafik = repo.getListDataGrafik()
    //    var dataHari = MutableLiveData<List<String>>("Senin","Selasa","Rabu","Kamis","Jumat","Sabtu","Minggu")
    val entries = MutableLiveData<List<Entry>?>()
    val getEntries: LiveData<List<Entry>?> = entries
    val listTanggal = MutableLiveData<ArrayList<String>>()
    val getListTanggal: LiveData<ArrayList<String>> = listTanggal
    var monthList = MutableLiveData<ArrayList<String>>(getMonthList())
    var getMonthList: LiveData<ArrayList<String>> = monthList
    //    val getDataBulan: LiveData<String> = dataBulan
    val getDataGrafik = repo.getDataGrafik()

//    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            requestData()
//        }
//    }
    fun clearGraphicData() {
        viewModelScope.launch {
            repo.deleteDataGrafik()
        }

    }
    fun getDataGrafikByDate(tanggal: String): List<GrafikEntity> {
        try {
            val listGrafik = repo.getListDataGrafikByDate(tanggal)
            var tanggalList = arrayListOf<String>()
            for (i in listGrafik){
               tanggalList.add(i.tanggal)
            }
            listTanggal.postValue(tanggalList)
            if(listGrafik.isNullOrEmpty()) entries.postValue(null)
             else entries.postValue(getEntry(listGrafik))
            Log.d("GrafikViewModel", "getDataGrafikByDate: $listGrafik")
        }catch (e: Exception){
        Log.d("REQUEST",e.message.toString())
//            status.postValue(ApiStatus.FAILED)
     }
        return repo.getListDataGrafikByDate(tanggal)
    }
    private fun getMonthList(): ArrayList<String>? {
        var listBulan = ArrayList<String>()
        listBulan.add("Januari")
        listBulan.add("Februari")
        listBulan.add("Maret")
        listBulan.add("April")
        listBulan.add("Mei")
        listBulan.add("Juni")
        listBulan.add("Juli")
        listBulan.add("Agusuts")
        listBulan.add("September")
        listBulan.add("Oktober")
        listBulan.add("November")
        listBulan.add("Desember")
        return listBulan
    }

    private fun requestData(){
        try {
            entries.postValue(getEntry(dataGrafik))
        }catch (e: Exception){
            Log.d("REQUEST",e.message.toString())
//            status.postValue(ApiStatus.FAILED)
        }
    }

    private fun getEntry(dataGrafik: List<GrafikEntity>): List<Entry> {
        var result = ArrayList<Entry>()

        var index = 1f
        for (i in dataGrafik){
            result.add(Entry(index,i.totalTransaksi.toFloat()))
//            if(getMonthList()!!.contains(i.bulan)){
//
//            }else{
//
//            }
            index+=1
//            dataBulan.value = i.bulan

//            }else currentMonth.value = getCurrentMonth()
        }
        Log.d("GrafikFragment", "getEntriesSize: ${result}")
//        monthList.postValue(listBulan)
        return result
    }
}


