package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pembayaran")
data class PembayaranEntity(
    @PrimaryKey(autoGenerate = true)
    val id_pembayaran : Long = 0L,

)
