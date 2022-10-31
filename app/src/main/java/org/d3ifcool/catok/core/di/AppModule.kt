package org.d3ifcool.catok.core.di

import androidx.room.Room
import org.d3ifcool.catok.core.data.source.local.db.CatokDao
import org.d3ifcool.catok.core.data.source.local.db.CatokDb
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single { Room.databaseBuilder(
        androidApplication(),
        CatokDb::class.java,
        "catok.db"
     ).build()
    }
    single<CatokDao> {
        val db = get<CatokDb>()
        db.getRunDao()
    }
}