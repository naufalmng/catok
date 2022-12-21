package org.d3ifcool.catok

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.d3ifcool.catok.core.data.source.local.db.CatokDao
import org.d3ifcool.catok.core.data.source.local.db.CatokDb
import org.d3ifcool.catok.utils.getCurrentDate
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.d3ifcool.catok.core.data.source.local.entities.*
import org.d3ifcool.catok.utils.getCurrentMonth
import org.junit.Before

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CatokDatabaseTest {
    private lateinit var db: CatokDb
    private lateinit var dao: CatokDao

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context,CatokDb::class.java).allowMainThreadQueries().build()
        dao = db.getRunDao()
    }

    @After
    fun closeDb(){
        db.close()
    }

    // Testing tambah,search,update,delete produk
    @Test
    fun testTambahUpdateDeleteProduk() = runTest{
        val dataProduk1 = ProdukEntity(id_produk = 1,barcode = null, namaProduk = "Tahu Isi", deskripsi = "Gorengan", modal = 500.0, hargaJual = 1000.0, satuan = 1, stok = 1, tanggal = getCurrentDate())
        val dataProduk2 = ProdukEntity(id_produk = 2,barcode = null, namaProduk = "Bala-Bala", deskripsi = "Gorengan", modal = 500.0, hargaJual = 1000.0, satuan = 1, stok = 1, tanggal = getCurrentDate())
        val dataProduk3 = ProdukEntity(id_produk = 3,barcode = null, namaProduk = "Cireng", deskripsi = "Gorengan", modal = 500.0, hargaJual = 1000.0, satuan = 1, stok = 1, tanggal = getCurrentDate())
        val updatedProduk1 = ProdukEntity(id_produk = 1,barcode = null, namaProduk = "Tahu Isi", deskripsi = "Gorengan", modal = 1000.0, hargaJual = 1500.0, satuan = 1, stok = 100, tanggal = getCurrentDate())
        val updatedProduk2 = ProdukEntity(id_produk = 2,barcode = null, namaProduk = "Bala-Bala", deskripsi = "Gorengan", modal = 1000.0, hargaJual = 1500.0, satuan = 1, stok = 100, tanggal = getCurrentDate())
        val updatedProduk3 = ProdukEntity(id_produk = 3,barcode = null, namaProduk = "Cireng", deskripsi = "Gorengan", modal = 1000.0, hargaJual = 1500.0, satuan = 1, stok = 100, tanggal = getCurrentDate())
        dao.insertData(dataProduk1)
        dao.insertData(dataProduk2)
        dao.insertData(dataProduk3)

        dao.searchProduk("tahu isi")
        val searchResult1 = dao.getArrayListDataProduk().getOrAwaitValue()
        assertThat(searchResult1).contains(dataProduk1)

        dao.searchProduk("bala-bala")
        val searchResult2 = dao.getArrayListDataProduk().getOrAwaitValue()
        assertThat(searchResult2).contains(dataProduk2)

        dao.searchProduk("cireng")
        val searchResult3 = dao.getArrayListDataProduk().getOrAwaitValue()
        assertThat(searchResult3).contains(dataProduk3)

        dao.updateData(updatedProduk1)
        dao.updateData(updatedProduk2)
        dao.updateData(updatedProduk3)

        dao.deleteData(listOf(1))
        dao.deleteData(listOf(2))
        dao.deleteData(listOf(3))

        dao.insertData(dataProduk1)
        dao.insertData(dataProduk2)
        dao.insertData(dataProduk3)

        dao.deleteData(listOf(1,2,3))

    }



    // Testing fungsionalitas utama aplikasi, tambah data produk, transaksi produk, tambah grafik
    @Test
    fun writeAndReadCatokDatabase() = runTest{
        val dataProduk = ProdukEntity(id_produk = 1,barcode = null, namaProduk = "Tahu Isi", deskripsi = "Gorengan", modal = 500.0, hargaJual = 1000.0, satuan = 1, stok = 1, tanggal = getCurrentDate())
        dao.insertData(dataProduk)
        val transaksi = TransaksiEntity(id_transaksi = 1, getCurrentDate())
        dao.insertTransaksi(transaksi)
        val transaksiProduk = TransaksiProdukEntity(id_produk = 1, id_transaksi = 1, qty = 1)
        dao.insertTransaksiProduk(transaksiProduk)
        val historiTransaksi = HistoriTransaksiEntity(id_histori = 1L, total = 2000.0, produkDibeli = "-Tahu Isi.", jumlahProdukDibeli = 1, invoice = "CTK123", bayar = 3000.0, kembalian = 1000.0, catatan = "", pembayaran = "CASH", statusBayar = "LUNAS", tanggal = getCurrentDate(), diskon = 0.0)
        dao.insertHistoriTransaksi(historiTransaksi)
        val dataGrafik = GrafikEntity(id = 1,2000.0, getCurrentMonth())
        dao.insertDataGrafik(dataGrafik)
        val allProduk = dao.getArrayListDataProduk().getOrAwaitValue()
        val allTransaksi = dao.getDataTransaksi().getOrAwaitValue()
        val allTransaksiProduk = dao.getListDataTransaksiProduk().getOrAwaitValue()
        val allHistoriTransaksi = dao.getDataHistoriTransaksi().getOrAwaitValue()
        val allGrafik = dao.getDataGrafik().getOrAwaitValue()
        assertThat(allProduk).contains(dataProduk)
        assertThat(allTransaksi).contains(transaksi)
        assertThat(allTransaksiProduk).contains(transaksiProduk)
        assertThat(allHistoriTransaksi).contains(historiTransaksi)
        assertThat(allGrafik).contains(dataGrafik)
    }
}