package org.d3ifcool.catok.core.data.source.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.core.data.source.local.entities.HistoriTransaksiEntity
import org.d3ifcool.catok.core.data.source.local.entities.TransaksiEntity
import org.d3ifcool.catok.core.data.source.local.entities.TransaksiProdukEntity

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

//    @Query("SELECT (SELECT COUNT(*) FROM produk)")
//    fun isDataProdukEmpty(): Int
//
//    @Query("SELECT (SELECT COUNT(*) FROM historiTransaksi)")
//    fun isDataTransaksiEmpty(): LiveData<Boolean>

    @Query("DELETE FROM produk WHERE id_produk IN (:ids)")
    suspend fun deleteData(ids: List<Int>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaksi(transaksi: TransaksiEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaksiProduk(transaksiProduk: TransaksiProdukEntity)

}