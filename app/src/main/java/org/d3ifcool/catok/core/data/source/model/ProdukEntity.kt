package org.d3ifcool.catok.core.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produk")
data class ProdukEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var barcode: Int? = null,
    var namaProduk: String,
    var deskripsi: String,
    var hargaBeli: Double,
    var hargaJual: Double,
    var satuan: Int,
    var stok: Int,
    var tanggal: String
)