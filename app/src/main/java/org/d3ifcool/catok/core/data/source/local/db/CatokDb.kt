package org.d3ifcool.catok.core.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.d3ifcool.catok.core.data.source.local.entities.ProdukEntity
import org.d3ifcool.catok.core.data.source.local.entities.HistoriTransaksiEntity
import org.d3ifcool.catok.core.data.source.local.entities.TransaksiEntity
import org.d3ifcool.catok.core.data.source.local.entities.TransaksiProdukEntity

@Database(
    entities = [ProdukEntity::class,TransaksiEntity::class,HistoriTransaksiEntity::class,TransaksiProdukEntity::class],
    version = 7,
    exportSchema = false
)
abstract class CatokDb : RoomDatabase() {
    abstract fun getRunDao(): CatokDao
}