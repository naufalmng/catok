package org.d3ifcool.catok.core.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filterGrafik")
data class FilterGrafikEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var date: String
)