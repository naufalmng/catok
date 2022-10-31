package org.d3ifcool.catok.core.di

import org.d3ifcool.catok.core.data.repository.AppRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { AppRepository(get()) }
}