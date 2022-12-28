package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "produk", indices = [Index(value = ["namaProduk"], unique = true)])
data class ProdukEntity (
    @PrimaryKey(autoGenerate = true)
    var id_produk: Int = 0,
    var barcode: Int? = null,
    var namaProduk: String,
    var deskripsi: String,
    var modal: Double,
    var hargaJual: Double,
    var satuan: Int,
    var satuanPer: String,
    var stok: Int,
    var tanggal: String
): Serializable