package org.d3ifcool.catok.core.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.d3ifcool.catok.core.data.source.local.db.CatokDao
import org.d3ifcool.catok.core.data.source.local.db.CatokDb
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private val roomCallback = object: RoomDatabase.Callback(){
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
//        db.execSQL(
//            "CREATE TRIGGER updateStok AFTER INSERT ON transaksiProduk" +
//                    "BEGIN " +
//                    "UPDATE produk SET stok = ${CatokApp.}"
//        )
    }
}

val appModule = module {
    single { Room.databaseBuilder(
        androidApplication(),
        CatokDb::class.java,
        "catok.db"
     ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }
    single<CatokDao> {
        val db = get<CatokDb>()
        db.getRunDao()
    }
}