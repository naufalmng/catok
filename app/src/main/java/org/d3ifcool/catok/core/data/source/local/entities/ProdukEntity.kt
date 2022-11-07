package org.d3ifcool.catok.core.data.source.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

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
): Serializable