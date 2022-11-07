package org.d3ifcool.catok.core.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity

@Database(
    entities = [ProdukEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CatokDb : RoomDatabase() {
    abstract fun getRunDao(): CatokDao
}