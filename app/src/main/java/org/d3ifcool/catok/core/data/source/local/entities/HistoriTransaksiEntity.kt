package org.d3ifcool.catok.core.data.source.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "historiTransaksi")
data class HistoriTransaksiEntity(
    @PrimaryKey
    val id_histori: String,
    var total: Double,
    var diskon: Double,
    var bayar: Double,
    var kembalian: Double,
    var catatan: String,
    var produkDibeli: String,
    var jumlahProdukDibeli: Int,
    var pembayaran: String,
    var statusBayar: String,
    var tanggal: String,
): Parcelable
