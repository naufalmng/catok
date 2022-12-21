package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksiEntity")
data class TransaksiEntity (
    @PrimaryKey(autoGenerate = true)
    var id_transaksi: Int = 0,
    var tanggal: String
)