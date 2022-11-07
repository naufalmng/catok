package org.d3ifcool.catok.core.data.repository

import org.d3ifcool.catok.core.data.source.local.db.CatokDao
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity

class AppRepository(private val catokDao: CatokDao) {
    suspend fun insertData(produk: ProdukEntity) = catokDao.insertData(produk)
    suspend fun updateData(produk: ProdukEntity) = catokDao.updateData(produk)
    fun getArrayListDataProduk() = catokDao.getArrayListDataProduk()
    fun isDataProdukEmpty() = catokDao.isDataProdukEmpty()
    suspend fun deleteData(newIds: List<Int>) = catokDao.deleteData(newIds)
}