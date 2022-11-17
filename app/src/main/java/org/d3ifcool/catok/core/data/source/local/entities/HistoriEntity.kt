package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Embedded
import androidx.room.PrimaryKey

data class HistoriEntity (
    @PrimaryKey(autoGenerate = true)
    val histori_id: Long = 0L,
    @Embedded
    val transaksi: TransaksiEntity

)