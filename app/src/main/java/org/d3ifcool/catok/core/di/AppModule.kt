@file:Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused"
)

package org.d3ifcool.catok.core.di

import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3ifcool.catok.core.data.source.local.db.CatokDb
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private val roomCallback = object: RoomDatabase.Callback(){
}

val appModule = module {
    single { Room.databaseBuilder(
        androidApplication(),
        CatokDb::class.java,
        "catok.db"
     ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }
    single {
        val db = get<CatokDb>()
        db.getRunDao()
    }
}