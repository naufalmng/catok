package org.d3ifcool.catok.core.data.source.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "historiTransaksi")
data class HistoriTransaksiEntity(
    @PrimaryKey(autoGenerate = true)
    val id_histori: Long = 0L,
    var total: Double,
    var diskon: Double,
    var bayar: Double,
    var kembalian: Double,
    var catatan: String,
    var produkDibeli: String,
    var jumlahProdukDibeli: Int,
    var invoice: String,
    var pembayaran: String,
    var statusBayar: String,
    var tanggal: String
): Parcelable
