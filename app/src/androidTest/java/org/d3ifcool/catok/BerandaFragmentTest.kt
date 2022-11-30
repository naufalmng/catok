package org.d3ifcool.catok

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3ifcool.catok.ui.beranda.BerandaFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BerandaFragmentTest {
    @Test
    fun testMenuDataProduk() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        val scenario = launchFragmentInContainer(themeResId = R.style.Theme_Catok){BerandaFragment().also {fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever{viewLifecycleOwner ->
                if(viewLifecycleOwner!=null){
                    navController.setGraph(R.navigation.catok_nav_graph)
                    Navigation.setViewNavController(fragment.requireView(),navController)
                }
            }
        }}

        // Click button data produk
        onView(withId(R.id.dataProduk))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    @Test
    fun testMenuTransaksiProduk()   {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        val scenario = launchFragmentInContainer(themeResId = R.style.Theme_Catok){BerandaFragment().also {fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever{viewLifecycleOwner ->
                if(viewLifecycleOwner!=null){
                    navController.setGraph(R.navigation.catok_nav_graph)
                    Navigation.setViewNavController(fragment.requireView(),navController)
                }
            }
        }}

        // Click button historiTransaksi produk
        onView(withId(R.id.transaksiProduk))
            .check(matches(isDisplayed()))
            .perform(click())


    }
}


//        // Click button back dari fragment data produk
//        onView(Matchers.allOf(
//            withContentDescription( "Navigate Up"),
//            isDisplayed()
//        )).perform(click())


//
//        // Click button tambah
//        onView(withId(R.id.btnTambah))
//            .check(matches(isDisplayed()))
//            .perform(click())
//
//        // set nama produk
//        onView(withId(R.id.etNamaProduk))
//            .check(matches(isDisplayed()))
//            .perform(replaceText("Terigu"))
//            .check(matches(withText("Terigu")))
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
//            .perform(replaceText("15000"))
//            .check(matches(withText("15000")))
//
//        // set harga jual produk
//        onView(withId(R.id.etHargaJual))
//            .check(matches(isDisplayed()))
//            .perform(replaceText("20000"))
//            .check(matches(withText("20000")))
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
//