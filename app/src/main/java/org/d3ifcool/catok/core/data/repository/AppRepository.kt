package org.d3ifcool.catok.core.data.repository

import org.d3ifcool.catok.core.data.source.local.db.CatokDao
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.core.data.source.local.entities.TransaksiEntity
import org.d3ifcool.catok.core.data.source.local.entities.TransaksiProdukEntity

class AppRepository(private val catokDao: CatokDao) {
    suspend fun insertData(produk: ProdukEntity) = catokDao.insertData(produk)
    suspend fun updateData(produk: ProdukEntity) = catokDao.updateData(produk)
    fun getArrayListDataProduk() = catokDao.getArrayListDataProduk()
    fun getArrayListDataTransaksi() = catokDao.getArrayListDataTransaksi()
//    fun isDataProdukEmpty() = catokDao.isDataProdukEmpty()
//    fun isDataTransaksiEmpty() = catokDao.isDataTransaksiEmpty()
    suspend fun deleteData(newIds: List<Int>) = catokDao.deleteData(newIds)
    suspend fun insertTransaksi(transaksi: TransaksiEntity) = catokDao.insertTransaksi(transaksi)
    suspend fun insertTransaksiProduk(transaksiproduk: TransaksiProdukEntity) = catokDao.insertTransaksiProduk(transaksiproduk)
}