@file:Suppress("RedundantSuspendModifier")

package org.d3ifcool.catok.core.data.repository

import android.graphics.Bitmap
import org.d3ifcool.catok.core.data.source.local.db.CatokDao
import org.d3ifcool.catok.core.data.source.local.entities.*

class AppRepository(private val catokDao: CatokDao) {
    suspend fun insertData(produk: ProdukEntity) = catokDao.insertData(produk)
    suspend fun updateData(produk: ProdukEntity) = catokDao.updateData(produk)
    fun getArrayListDataProduk() = catokDao.getArrayListDataProduk()
    suspend fun deleteData(newIds: List<Int>) = catokDao.deleteData(newIds)
    suspend fun insertTransaksi(transaksi: TransaksiEntity) = catokDao.insertTransaksi(transaksi)
    suspend fun insertTransaksiProduk(transaksiproduk: TransaksiProdukEntity) = catokDao.insertTransaksiProduk(transaksiproduk)
    fun getListTransaksi(): List<TransaksiEntity> = catokDao.getListTransaksi()
    suspend fun insertHistoriTransaksi(historiTransaksi: HistoriTransaksiEntity) = catokDao.insertHistoriTransaksi(historiTransaksi)
    suspend fun updateStok(idProduk: Int, qty: Int) = catokDao.updateStok(idProduk,qty)
    suspend fun insertDataGrafik(grafikEntity: GrafikEntity) = catokDao.insertDataGrafik(grafikEntity)
    suspend fun updateDataGrafik(totalTransaksi: Double,tanggal: String) = catokDao.updateDataGrafik(totalTransaksi,tanggal)
    fun getListDataGrafik() = catokDao.getListDataGrafik()
    fun getListDataGrafikByDate(bulanDanTahun: String) = catokDao.getListDataGrafikByDate(bulanDanTahun)
    fun getDataGrafik() = catokDao.getDataGrafik()
    suspend fun insertProfil(profil: ProfilEntity) = catokDao.insertProfil(profil)
    suspend fun updateProfil(nama: String,gambar: Bitmap) = catokDao.updateProfil(nama,gambar)
    fun getProfil() = catokDao.getProfil()
    fun searchProduk(query: String) = catokDao.searchProduk(query)
    fun getListDataTransaksiProduk() = catokDao.getListDataTransaksiProduk()
    fun getDataTransaksi() = catokDao.getDataTransaksi()
    fun getArrayListDataHistoriTransaksi() = catokDao.getDataHistoriTransaksi()
    fun getArrayListDataHistoriTransaksiByStatusBayar() = catokDao.getDataHistoriTransaksiByStatusBayar("LUNAS")
    suspend fun returTransaksi(id: Int,qty: Int) = catokDao.returTransaksi(id,qty)
    suspend fun insertReturData(returEntity: ReturEntity) = catokDao.insertReturData(returEntity)
    fun getReturEntity() = catokDao.getReturEntity()
    suspend fun deleteGrafikDataById(invoice: String) = catokDao.deleteDataGrafikById(invoice)
    suspend fun deleteTransaksiById(idTransaksi: String) = catokDao.deleteTransaksiById(idTransaksi)
    suspend fun deleteTransaksiProdukById(idHistori: String) = catokDao.deleteTransaksiProdukById(idHistori)
    suspend fun updateHistoriTransaksiByInvoice(idHistori: String) = catokDao.updateHistoriTransaksi("DIRETUR",idHistori)


}