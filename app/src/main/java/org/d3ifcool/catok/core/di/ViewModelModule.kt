package org.d3ifcool.catok.core.di

import org.d3ifcool.catok.ui.beranda.produk.DataProdukViewModel
import org.d3ifcool.catok.ui.beranda.transaksi.TransaksiViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{DataProdukViewModel(get())}
    viewModel{TransaksiViewModel(get())}
}