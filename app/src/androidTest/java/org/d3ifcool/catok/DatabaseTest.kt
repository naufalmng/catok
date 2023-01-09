package org.d3ifcool.catok

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skydoves.powerspinner.DefaultSpinnerAdapter
import kotlinx.coroutines.delay
import okhttp3.internal.wait
import org.d3ifcool.catok.ui.beranda.transaksi.TransaksiAdapter
import org.d3ifcool.catok.ui.main.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    var context: Context? = null
    @get:Rule
    val activityScenario = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

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

        insertDataProduk("Terigu","Bahan-Bahan","15000","20000","1","100")
        insertDataProduk("Gehu Pedas","Gorengan","500","1000","1","100")
        insertDataProduk("Cireng","Gorengan","500","1000","1","100")
        insertDataProduk("Bala-Bala","Gorengan","500","1000","1","100")
        insertDataProduk("Tempe","Gorengan","500","1000","1","100")
    }
    fun insertDataProduk(namaProduk: String,deskripsi: String,modal: String,hargaJual: String,satuan: String,stokAwal: String){
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

    fun insertWrongDataProduk(namaProduk: String,deskripsi: String,modal: String,hargaJual: String,satuan: String,stokAwal: String){
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

    private fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }
    @Ignore
    @Test
    fun testTransaksi(){
        onView(withId(R.id.transaksiProduk))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(click())

        onView(withId(R.id.btnTambah))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(click())

        onView(withId(R.id.recyclerViewDataTransaksi)).perform(RecyclerViewActions.actionOnItemAtPosition<TransaksiAdapter.LinearViewHolder>(0,clickChildViewWithId(R.id.plus)))

        onView(withId(R.id.btnSelanjutnya))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(click())

        onView(withId(R.id.etBayar))
            .perform(typeText("2000"))
            .perform(ViewActions.closeSoftKeyboard())
            .perform(click())

        onView(withId(R.id.btnSimpan))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(click())

    }

    fun clickChildViewWithId(id: Int): ViewAction {
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

    class ClickOnButtonView(private val id: Int) : ViewAction {
        private var click = ViewActions.longClick()

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