package org.d3ifcool.catok.core.di

import org.d3ifcool.catok.ui.beranda.produk.DataProdukViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{DataProdukViewModel(get())}
}