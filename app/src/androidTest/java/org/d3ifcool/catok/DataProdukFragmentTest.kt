package org.d3ifcool.catok

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.AndroidJUnit4
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import org.d3ifcool.catok.TestUtils.TestUtils.withRecyclerView
import org.d3ifcool.catok.ui.beranda.produk.DataProdukAdapter
import org.d3ifcool.catok.ui.beranda.produk.DataProdukDialog
import org.d3ifcool.catok.ui.main.MainActivity
import org.d3ifcool.catok.utils.CatokApp
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock


@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class DataProdukFragmentTest {

    @get:Rule
    val activityScenario = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Before
    fun init() {
        activityScenario.scenario.onActivity {
            it.supportFragmentManager.beginTransaction()
        }
    }

    @Test
    fun testNavigasiGrafik(){
        onView(withId(R.id.grafikFragment))
            .check(matches(isDisplayed()))
            .perform(click())
    }
    @Test
    fun testUbahProfil(){
        onView(withId(R.id.pengaturanFragment))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.etNamaToko))
            .perform(replaceText("Toko Naufal"))
            .check(matches(withText("Toko Naufal")))

        onView(withText("OK"))
            .check(matches(isDisplayed()))
            .perform(click())

//        intended(allOf(hasAction(equalTo(Intent.ACTION_GET_CONTENT)), hasType(is("image/*")))
    }

    @Test
    fun testSearchDataProduk() {
        onView(withId(R.id.dataProduk))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.action_search)).perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeText("bala-bala"))
        onView(withRecyclerView(R.id.recyclerViewDataProduk).atPositionOnView(0,R.id.namaProduk)).check(matches(withText("Bala-Bala")))
    }

    @Test
    fun testTambahProduk() {
        val args = Bundle()
        args.putBoolean("isInsert", true)
        args.putSerializable("produk", null)

        val mockNavController = mock(NavController::class.java)

        val titleScenario = launchFragmentInContainer<DataProdukDialog>(
            themeResId = R.style.Theme_Catok,
            fragmentArgs = args
        )

        titleScenario.onFragment { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    mockNavController.setGraph(R.navigation.catok_nav_graph)
                    Navigation.setViewNavController(fragment.requireView(), mockNavController)
                }
            }
        }

        // set nama produk
        onView(withId(R.id.etNamaProduk))
            .check(matches(isDisplayed()))
            .perform(replaceText("Terigu"))
            .check(matches(withText("Terigu")))

        // set deskripsi produk
        onView(withId(R.id.etDeskripsi))
            .check(matches(isDisplayed()))
            .perform(replaceText("Bahan-Bahan"))
            .check(matches(withText("Bahan-Bahan")))

        // set modal produk
        onView(withId(R.id.etModal))
            .check(matches(isDisplayed()))
            .perform(replaceText("15000"))
            .check(matches(withText("Rp․ 15.000")))

        // set harga jual produk
        onView(withId(R.id.etHargaJual))
            .check(matches(isDisplayed()))
            .perform(replaceText("20000"))
            .check(matches(withText("Rp․ 20.000")))

        // set satuan produk
        onView(withId(R.id.etSatuan))
            .check(matches(isDisplayed()))
            .perform(replaceText("1"))
            .check(matches(withText("1")))

        onView(withId(R.id.spinnerSatuanPer)).perform(click())
        onView(withText(R.array.jenis_satuan)).perform(click())
//        onData(anything()).atPosition(1).perform(click())
//        onView(withId(R.id.spinnerSatuanPer)).check(matches(withSpinnerText(containsString("Pcs"))))


        // set stok awal produk
        onView(withId(R.id.etStokAwal))
            .check(matches(isDisplayed()))
            .perform(replaceText("100"))
            .check(matches(withText("100")))

        // Click button simpan
        onView(withId(R.id.btnSimpan))
            .check(matches(isDisplayed()))
            .perform(click())

//        // set nama produk
//        onView(withId(R.id.etNamaProduk))
//            .check(matches(isDisplayed()))
//            .perform(replaceText("Bawang"))
//            .check(matches(withText("Bawang")))
//
//        // set deskripsi produk
//        onView(withId(R.id.etDeskripsi))
//            .check(matches(isDisplayed()))
//            .perform(replaceText("Bahan-Bahan"))
//            .check(matches(withText("Bahan-Bahan")))
//
//        // set modal produk
//        onView(withId(R.id.etModal))
//            .check(matches(isDisplayed()))
//            .perform(replaceText("10000"))
//            .check(matches(withText("Rp․ 10.000")))
//
//        // set harga jual produk
//        onView(withId(R.id.etHargaJual))
//            .check(matches(isDisplayed()))
//            .perform(replaceText("15000"))
//            .check(matches(withText("Rp․ 15.000")))
//
//        // set modal produk
//        onView(withId(R.id.etSatuan))
//            .check(matches(isDisplayed()))
//            .perform(replaceText("1"))
//            .check(matches(withText("1")))
//
//        // set satuan produk
//        onView(withId(R.id.etSatuan))
//            .check(matches(isDisplayed()))
//            .perform(replaceText("1"))
//            .check(matches(withText("1")))
//
//        // set stok awal produk
//        onView(withId(R.id.etStokAwal))
//            .check(matches(isDisplayed()))
//            .perform(replaceText("100"))
//            .check(matches(withText("100")))
//
//        // Click button simpan
//        onView(withId(R.id.btnSimpan))
//            .check(matches(isDisplayed()))
//            .perform(click())

        Log.d("TestTambahDataProduk", "Produk berhasil ditambah !")

    }

    @Test
    fun deleteProduk(){
        onView(withId(R.id.dataProduk))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.recyclerViewDataProduk)).perform(
            actionOnItemAtPosition<DataProdukAdapter.LinearViewHolder>(
                0,
                longClick()
            )
        )

        onView(withId(R.id.menu_delete)).perform(click())

        // set nama produk
        onView(withText("Hapus")).perform(click())
    }

    @Test
    fun testEditProduk() {
        onView(withId(R.id.dataProduk))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.recyclerViewDataProduk)).perform(
            actionOnItemAtPosition<DataProdukAdapter.LinearViewHolder>(
                0,
                longClick()
            )
        )

        onView(withId(R.id.menu_edit)).perform(click())

        // set nama produk
        onView(withId(R.id.etNamaProduk))
            .check(matches(isDisplayed()))
            .perform(replaceText("Bawang Putih"))
            .check(matches(withText("Bawang Putih")))

        // Click button simpan
        onView(withId(R.id.btnSimpan))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    @Test
    fun testPilihSemuaDataProduk() {
        onView(withId(R.id.dataProduk))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.recyclerViewDataProduk)).perform(
            actionOnItemAtPosition<DataProdukAdapter.LinearViewHolder>(
                0,
                longClick()
            )
        )
        onView(withId(R.id.menu_select_all)).perform(click())

    }

    private fun clickActionMode() = object : ViewAction {
        val device = UiDevice.getInstance(getInstrumentation())
        val context: Context = ApplicationProvider.getApplicationContext<CatokApp>()
        private var longClick = longClick()
        val btnMenu: UiObject =
            device.findObject(UiSelector().description(context.getString(R.string.select_all)))

        override fun getConstraints(): Matcher<View> {
            return longClick.constraints
        }

        override fun getDescription(): String {
            return " click on custom button view"
        }

        override fun perform(uiController: UiController?, view: View?) {
//            longClick.perform()
        }
    }
}

//    private fun clickChildViewWithId(id: Int): ViewAction {
//        return object : ViewAction {
//            override fun getConstraints(): Matcher<View>? {
//                return null
//            }
//
//            override fun getDescription(): String {
//                return "Click on a child view with specified id."
//            }
//
//            override fun perform(uiController: UiController, view: View) {
//                val v = view.findViewById<View>(id)
//                v.performLongClick()
//            }
//        }
//    }

private fun clickOnButtonAtRow(position: Int) {
    onView(withId(R.id.recyclerViewDataProduk)).perform(
        actionOnItemAtPosition<DataProdukAdapter.LinearViewHolder>(
            0,
            longClick()
        )
    )
//            (position, ClickOnButtonView()))
}

class ClickOnButtonView() : ViewAction {
    private var click = longClick()

    override fun getConstraints(): Matcher<View> {
        return click.constraints
    }

    override fun getDescription(): String {
        return " click on custom button view"
    }

    override fun perform(uiController: UiController, view: View) {
        //btnClickMe -> Custom row item view button
        click.perform(uiController, view)
    }
}
