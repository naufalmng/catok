package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Entity

@Entity(primaryKeys = ["id_produk","id_transaksi"])
data class TransaksiProdukEntity (
    val id_produk: Int,
    val id_transaksi: Long,
    val qty: Int,
)