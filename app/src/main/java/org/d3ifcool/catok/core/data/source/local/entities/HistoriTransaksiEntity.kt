package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "historiTransaksi")
data class HistoriTransaksiEntity(
    @PrimaryKey(autoGenerate = true)
    val id_histori : Long = 0L,
    var total: Double,
    var produkDibeli: String,
    var invoice: String,
    var pembayaran: String,
    var statusBayar: String,
    var tanggal: Long
)
