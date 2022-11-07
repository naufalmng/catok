package org.d3ifcool.catok.core.data.source.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity

@Dao
interface CatokDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(produk: ProdukEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateData(produk: ProdukEntity)

    @Query("SELECT * FROM produk")
    fun getArrayListDataProduk(): LiveData<List<ProdukEntity>>

    @Query("SELECT (SELECT COUNT(*) FROM produk) == 0")
    fun isDataProdukEmpty(): LiveData<Boolean>

    @Query("DELETE FROM produk WHERE id IN (:ids)")
    suspend fun deleteData(ids: List<Int>)

}