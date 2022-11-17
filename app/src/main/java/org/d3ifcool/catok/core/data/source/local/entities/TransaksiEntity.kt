package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "transaksi")
data class TransaksiEntity(
    @PrimaryKey(autoGenerate = true)
    var id_transaksi : Long = 0L,
    var produk: List<ProdukEntity>,
    var pembayaran: PembayaranEntity,
    var tanggal: Long
)
