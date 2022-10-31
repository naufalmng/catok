package org.d3ifcool.catok.core.data.repository

import androidx.lifecycle.asLiveData
import org.d3ifcool.catok.core.data.source.local.db.CatokDao
import org.d3ifcool.catok.core.data.source.model.ProdukEntity

class AppRepository(private val catokDao: CatokDao) {
    suspend fun insertData(produk: ProdukEntity) = catokDao.insertData(produk)
    fun getArrayListDataProduk() = catokDao.getArrayListDataProduk()
    fun isDataProdukEmpty() = catokDao.isDataProdukEmpty()
}