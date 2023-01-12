package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grafikEntity")
data class GrafikEntity(
    @PrimaryKey
    var id: String,
    var totalTransaksi: Double,
    var tanggal: Long = System.currentTimeMillis(),
    var tanggal2: String,
    var bulanDanTahun: String,
)
