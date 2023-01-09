package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksiEntity")
data class TransaksiEntity (
    @PrimaryKey()
    var id_transaksi: String,
    var tanggal: String
)