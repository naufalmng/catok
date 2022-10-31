package org.d3ifcool.catok.core.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.d3ifcool.catok.core.data.source.model.ProdukEntity

@Database(
    entities = [ProdukEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CatokDb : RoomDatabase() {
    abstract fun getRunDao(): CatokDao
}