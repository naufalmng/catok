package org.d3ifcool.catok.core.data.source.local.db

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.d3ifcool.catok.core.data.source.local.entities.*
import java.util.logging.Filter

@Dao
interface CatokDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(produk: ProdukEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateData(produk: ProdukEntity)

    @Query("SELECT * FROM produk")
    fun getArrayListDataProduk(): LiveData<List<ProdukEntity>>

    @Query("SELECT * FROM historiTransaksi")
    fun getArrayListDataTransaksi(): LiveData<List<HistoriTransaksiEntity>>

    @Query("SELECT * FROM historiTransaksi")
    fun getListDataTransaksi(): List<HistoriTransaksiEntity>

//    @Query("SELECT (SELECT COUNT(*) FROM produk)")
//    fun isDataProdukEmpty(): Int
//
//    @Query("SELECT (SELECT COUNT(*) FROM historiTransaksi)")
//    fun isDataTransaksiEmpty(): LiveData<Boolean>

    @Query("DELETE FROM produk WHERE id_produk IN (:ids)")
    suspend fun deleteData(ids: List<Int>)

//    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaksi(transaksi: TransaksiEntity)

//    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaksiProduk(transaksiProduk: TransaksiProdukEntity)

    @Query("SELECT * FROM transaksiEntity")
    fun getListTransaksi(): List<TransaksiEntity>

    @Query("SELECT * FROM transaksiEntity")
    fun getDataTransaksi(): LiveData<List<TransaksiEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoriTransaksi(historiTransaksi: HistoriTransaksiEntity)

    @Query("UPDATE produk SET stok = stok-:qty WHERE id_produk=:id")
    suspend fun updateStok(id: Int, qty: Int)

    @Query("DELETE FROM transaksiEntity")
    suspend fun deleteTransaksi()

    @Query("DELETE FROM historiTransaksi")
    suspend fun deleteHistoriTransaksi()

    @Query("DELETE FROM transaksiProduk")
    suspend fun deleteTransaksiProdukEntity()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataGrafik(grafikEntity: GrafikEntity)

    @Query("SELECT * FROM grafikEntity")
    fun getListDataGrafik(): List<GrafikEntity>

    @Query("SELECT * FROM grafikEntity WHERE tanggal=:tanggal")
    fun getListDataGrafikByDate(tanggal: String): List<GrafikEntity>

    @Query("SELECT * FROM grafikEntity")
    fun getDataGrafik(): LiveData<List<GrafikEntity>>

    @Query("DELETE FROM grafikEntity")
    suspend fun deleteDataGrafik()

    @Query("UPDATE grafikEntity SET totalTransaksi = :totalTransaksi WHERE tanggal=:tanggal")
    suspend fun updateDataGrafik(totalTransaksi: Double,tanggal: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfil(profil: ProfilEntity)
    @Query("UPDATE profilEntity SET namaToko = :nama,gambar = :gambar WHERE id = 1 ")
    fun updateProfil(nama: String, gambar: Bitmap)

    @Query("SELECT * FROM profilEntity")
    fun getProfil(): ProfilEntity

    @Query("UPDATE produk SET stok = stok+:qty WHERE id_produk=:id")
    suspend fun returTransaksi(id:Int,qty: Int)

    @Query("SELECT * FROM profilEntity")
    fun getDataProfil(): LiveData<List<ProfilEntity>>
    @Query("SELECT * FROM produk WHERE namaProduk LIKE :searchQuery OR deskripsi LIKE :searchQuery OR hargaJual LIKE :searchQuery OR stok LIKE :searchQuery")
    fun searchProduk(searchQuery: String): Flow<List<ProdukEntity>>

    @Query("SELECT * FROM transaksiProduk")
    fun getListDataTransaksiProduk(): LiveData<List<TransaksiProdukEntity>>

    @Query("SELECT * FROM historiTransaksi")
    fun getDataHistoriTransaksi(): LiveData<List<HistoriTransaksiEntity>>

    @Query("SELECT * FROM historiTransaksi WHERE tanggal = :date")
    fun getDataHistoriTransaksiByDate(date: String): LiveData<List<HistoriTransaksiEntity>>

    @Query("SELECT * FROM filterGrafik ORDER BY id ASC")
    fun getFilterGrafik(): LiveData<List<FilterGrafikEntity>>

    @Query("DELETE FROM filterGrafik")
    fun clearFilterGrafik()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilterGrafik(filterGrafik: FilterGrafikEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReturData(returEntity: ReturEntity)

    @Query("SELECT * FROM returEntity")
    fun getReturEntity(): LiveData<List<ReturEntity>>

    @Query("DELETE FROM historiTransaksi WHERE id_histori = :invoice")
    suspend fun deleteHistoriTransaksiByInvoice(invoice: String)
    @Query("DELETE FROM grafikEntity WHERE id=:invoice")
    suspend fun deleteDataGrafikById(invoice: String)
    @Query("DELETE FROM transaksiEntity WHERE id_transaksi = :idTransaksi")
    suspend fun deleteTransaksiById(idTransaksi: String)
    @Query("DELETE FROM transaksiProduk WHERE id_transaksi =:idHistori")
    suspend fun deleteTransaksiProdukById(idHistori: String)

}