package org.d3ifcool.catok.core.data.source.local.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profilEntity")
data class ProfilEntity (
    @PrimaryKey()
    var id: Int = 1,
    var namaToko: String,
    var gambar: Bitmap
    )