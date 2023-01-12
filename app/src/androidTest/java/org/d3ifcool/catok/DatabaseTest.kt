@file:Suppress("RedundantNullableReturnType", "unused")

package org.d3ifcool.catok

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3ifcool.catok.ui.beranda.transaksi.TransaksiAdapter
import org.d3ifcool.catok.ui.main.MainActivity
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private var context: Context? = null
    @get:Rule
    val activityScenario = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        activityScenario.scenario.onActivity {
            context = it.applicationContext
            it.supportFragmentManager.beginTransaction()
        }
    }


    @Test
    fun testTambahProduk() {
        onView(withId(R.id.dataProduk))
            .check(matches(isDisplayed()))
            .perform(click())

        insertDataProduk("Tempe Goreng","Gorengan","500","1000","1","100")
        insertDataProduk("Gehu Pedas","Gorengan","500","1000","1","100")
        insertDataProduk("Cireng","Gorengan","500","1000","1","100")
        insertDataProduk("Bala-Bala","Gorengan","500","1000","1","100")
        insertDataProduk("Pisang Goreng","Gorengan","500","1000","1","100")
        insertDataProduk("Molen","Gorengan","500","1000","1","100")
        insertDataProduk("Ubi Goreng","Gorengan","500","1000","1","100")
        insertDataProduk("Tahu Isi","Gorengan","500","1000","1","100")
        insertDataProduk("Singkong Goreng","Gorengan","500","1000","1","100")
        insertDataProduk("Combro","Gorengan","500","1000","1","100")
        insertDataProduk("Misro","Gorengan","500","1000","1","100")
    }
    private fun insertDataProduk(namaProduk: String, deskripsi: String, modal: String, hargaJual: String, satuan: String, stokAwal: String){
        // set nama produk
        onView(withId(R.id.btnTambah))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.etNamaProduk))
            .check(matches(isDisplayed()))
            .perform(replaceText(namaProduk))
            .check(matches(withText(namaProduk)))

        // set deskripsi produk
        onView(withId(R.id.etDeskripsi))
            .check(matches(isDisplayed()))
            .perform(replaceText(deskripsi))
            .check(matches(withText(deskripsi)))

        // set modal produk
        onView(withId(R.id.etModal))
            .check(matches(isDisplayed()))
            .perform(replaceText(modal))

        // set harga jual produk
        onView(withId(R.id.etHargaJual))
            .check(matches(isDisplayed()))
            .perform(replaceText(hargaJual))

        // set satuan produk
        onView(withId(R.id.etSatuan))
            .check(matches(isDisplayed()))
            .perform(replaceText(satuan))
            .check(matches(withText(satuan)))

        // set stok awal produk
        onView(withId(R.id.etStokAwal))
            .check(matches(isDisplayed()))
            .perform(replaceText(stokAwal))
            .check(matches(withText(stokAwal)))

        onView(withId(R.id.btnSimpan))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    @Test
    fun testTambahProdukSalah(){
        onView(withId(R.id.dataProduk))
            .check(matches(isDisplayed()))
            .perform(click())
        insertWrongDataProduk("","Gorengan","500","1000","1","100")
        insertWrongDataProduk("Tempe","","500","1000","1","100")
        insertWrongDataProduk("Tempe","Gorengan","","1000","1","100")
        insertWrongDataProduk("Tempe","Gorengan","500","","1","100")
        insertWrongDataProduk("Tempe","Gorengan","500","1000","","")
    }

    private fun insertWrongDataProduk(namaProduk: String, deskripsi: String, modal: String, hargaJual: String, satuan: String, stokAwal: String){
        // set nama produk
        onView(withId(R.id.btnTambah))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.etNamaProduk))
            .check(matches(isDisplayed()))
            .perform(replaceText(namaProduk))
            .check(matches(withText(namaProduk)))

        // set deskripsi produk
        onView(withId(R.id.etDeskripsi))
            .check(matches(isDisplayed()))
            .perform(replaceText(deskripsi))
            .check(matches(withText(deskripsi)))

        // set modal produk
        if (modal!=""){
            onView(withId(R.id.etModal))
                .check(matches(isDisplayed()))
                .perform(replaceText(modal))
        }
        if (hargaJual!=""){
            // set harga jual produk
            onView(withId(R.id.etHargaJual))
                .check(matches(isDisplayed()))
                .perform(replaceText(hargaJual))
        }

        if (satuan!=""){
            // set satuan produk
            onView(withId(R.id.etSatuan))
                .check(matches(isDisplayed()))
                .perform(replaceText(satuan))
                .check(matches(withText(satuan)))
        }

        if (stokAwal!=""){
            // set stok awal produk
            onView(withId(R.id.etStokAwal))
                .check(matches(isDisplayed()))
                .perform(replaceText(stokAwal))
                .check(matches(withText(stokAwal)))
        }

        onView(withId(R.id.btnSimpan))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.btnBatalSimpan))
            .check(matches(isDisplayed()))
            .perform(click())

    }

    @SuppressLint("IgnoreWithoutReason")
    @Ignore
    @Test
    fun testTransaksi(){
        onView(withId(R.id.transaksiProduk))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.btnTambah))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.recyclerViewDataTransaksi)).perform(RecyclerViewActions.actionOnItemAtPosition<TransaksiAdapter.LinearViewHolder>(0,clickChildViewWithId(R.id.plus)))

        onView(withId(R.id.btnSelanjutnya))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.etBayar))
            .perform(typeText("2000"))
            .perform(closeSoftKeyboard())
            .perform(click())

        onView(withId(R.id.btnSimpan))
            .check(matches(isDisplayed()))
            .perform(click())

    }

    private fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }

    class ClickOnButtonView : ViewAction {
        private var click = longClick()

        override fun getConstraints(): Matcher<View> {
            return click.constraints
        }

        override fun getDescription(): String {
            return " click on custom button view"
        }

        override fun perform(uiController: UiController, view: View) {
            //btnClickMe -> Custom row item view button
//            val v = view.findViewById(id
        }
    }
}