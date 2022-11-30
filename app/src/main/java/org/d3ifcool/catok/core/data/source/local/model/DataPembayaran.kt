package org.d3ifcool.catok.core.data.source.local.model

import androidx.room.Entity

class DataPembayaran (
    val nomor: Int,
    val produk: String,
    val qty: Int,
    val harga: Double,
)