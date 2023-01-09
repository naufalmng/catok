package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "returEntity")
data class ReturEntity (
    @PrimaryKey(autoGenerate = true)
    var idRetur: Int = 0,
    var idProduk: List<Int>,
    var idTransaksi: String,
    var qty: List<Int>
)