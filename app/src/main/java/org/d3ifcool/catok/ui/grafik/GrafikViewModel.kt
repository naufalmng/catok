package org.d3ifcool.catok.ui.grafik
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import org.d3ifcool.catok.core.data.repository.AppRepository
import org.d3ifcool.catok.core.data.source.local.entities.GrafikEntity

class GrafikViewModel(private val repo: AppRepository): ViewModel() {
    val selectedMonthAndYear = MutableLiveData<String>()
    private val entries = MutableLiveData<List<Entry>?>()
    val getEntries: LiveData<List<Entry>?> = entries
    val getDataGrafik = repo.getDataGrafik()
    fun getDataGrafikByDate(bulanDanTahun: String): List<GrafikEntity> {
        try {
            val listGrafik = repo.getListDataGrafikByDate(bulanDanTahun)
            if(listGrafik.isNullOrEmpty()) entries.postValue(null)
             else entries.postValue(getEntry(listGrafik))
            Log.d("GrafikViewModel", "getDataGrafikByDate: $listGrafik")
        }catch (e: Exception){
        Log.d("REQUEST",e.message.toString())
     }
        return repo.getListDataGrafikByDate(bulanDanTahun)
    }

    private fun getEntry(dataGrafik: List<GrafikEntity>): List<Entry> {
        val result = ArrayList<Entry>()

        var index = 1f
        for (i in dataGrafik){
            result.add(Entry(index,i.totalTransaksi.toFloat()))
            index+=1
        }
        Log.d("GrafikFragment", "getEntriesSize: $result")
        return result
    }
}


