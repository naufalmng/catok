package org.d3ifcool.catok.core.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.d3ifcool.catok.core.data.source.local.entities.*
import org.d3ifcool.catok.utils.Converters

@Database(
    entities = [ProdukEntity::class,TransaksiEntity::class,HistoriTransaksiEntity::class,TransaksiProdukEntity::class,GrafikEntity::class,ProfilEntity::class],
    version = 19,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CatokDb : RoomDatabase() {
    abstract fun getRunDao(): CatokDao
}