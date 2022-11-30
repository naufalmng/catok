package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransaksiEntity (
    @PrimaryKey(autoGenerate = true)
    val id_transaksi: Long = 0L,
    val tanggal: String
)