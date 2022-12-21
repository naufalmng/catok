package org.d3ifcool.catok.core.data.source.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.d3ifcool.catok.core.data.source.local.entities.*

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

    @Query("SELECT * FROM grafikEntity")
    fun getDataGrafik(): LiveData<List<GrafikEntity>>

    @Query("DELETE FROM grafikEntity")
    suspend fun deleteDataGrafik()

    @Query("UPDATE grafikEntity SET totalTransaksi = totalTransaksi+:totalSekarang WHERE id=:id")
    suspend fun updateDataGrafik(id: Int, totalSekarang: Double)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfil(profil: ProfilEntity)

    @Query("SELECT * FROM profilEntity")
    fun getProfil(): ProfilEntity

    @Query("SELECT * FROM profilEntity")
    fun getDataProfil(): LiveData<List<ProfilEntity>>
    @Query("SELECT * FROM produk WHERE namaProduk LIKE :searchQuery OR deskripsi LIKE :searchQuery OR hargaJual LIKE :searchQuery OR stok LIKE :searchQuery")
    fun searchProduk(searchQuery: String): Flow<List<ProdukEntity>>

    @Query("SELECT * FROM transaksiProduk")
    fun getListDataTransaksiProduk(): LiveData<List<TransaksiProdukEntity>>

    @Query("SELECT * FROM historiTransaksi")
    fun getDataHistoriTransaksi(): LiveData<List<HistoriTransaksiEntity>>

}