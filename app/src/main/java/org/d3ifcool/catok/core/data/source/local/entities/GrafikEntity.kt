package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grafikEntity")
data class GrafikEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var totalTransaksi: Double,
    var tanggal: String,
)
