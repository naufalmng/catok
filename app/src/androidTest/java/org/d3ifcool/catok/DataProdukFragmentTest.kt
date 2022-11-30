package org.d3ifcool.catok

import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3ifcool.catok.ui.beranda.BerandaFragment
import org.d3ifcool.catok.ui.beranda.produk.DataProdukDialog
import org.d3ifcool.catok.ui.beranda.produk.DataProdukFragment
import org.hamcrest.Matcher
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class DataProdukFragmentTest {
    @Ignore
    @Test
    fun testTambahProduk(){
        val args = Bundle()
        args.putBoolean("isInsert",true)
        args.putSerializable("produk",null)

        val mockNavController = mock(NavController::class.java)

        val titleScenario = launchFragmentInContainer<DataProdukDialog>(themeResId = R.style.Theme_Catok, fragmentArgs = args)

        titleScenario.onFragment{fragment->
            fragment.viewLifecycleOwnerLiveData.observeForever{viewLifecycleOwner ->
                if(viewLifecycleOwner!=null){
                    mockNavController.setGraph(R.navigation.catok_nav_graph)
                    Navigation.setViewNavController(fragment.requireView(),mockNavController)
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

        // set modal produk
        onView(withId(R.id.etSatuan))
            .check(matches(isDisplayed()))
            .perform(replaceText("1"))
            .check(matches(withText("1")))

        // set satuan produk
        onView(withId(R.id.etSatuan))
            .check(matches(isDisplayed()))
            .perform(replaceText("1"))
            .check(matches(withText("1")))

        // set stok awal produk
        onView(withId(R.id.etStokAwal))
            .check(matches(isDisplayed()))
            .perform(replaceText("100"))
            .check(matches(withText("100")))

        // Click button simpan
        onView(withId(R.id.btnSimpan))
            .check(matches(isDisplayed()))
            .perform(click())

    }

    @Test
    fun testEditProduk(){
        val args = Bundle()
        args.putBoolean("isInsert",false)
        args.putSerializable("produk",null)

        val mockNavController = mock(NavController::class.java)

        val titleScenario = launchFragmentInContainer<DataProdukFragment>(themeResId = R.style.Theme_Catok)

        titleScenario.onFragment{fragment->
            fragment.viewLifecycleOwnerLiveData.observeForever{viewLifecycleOwner ->
                if(viewLifecycleOwner!=null){
                    mockNavController.setGraph(R.navigation.catok_nav_graph)
                    Navigation.setViewNavController(fragment.requireView(),mockNavController)
                }
            }
        }

        onView(withId(R.id.recyclerViewDataProduk)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, longClick()))
        onView(withText("Edit Produk")).perform(click())

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

    @Ignore
    @Test
    fun testPilihSemuaDataProduk(){
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        val scenario = launchFragmentInContainer(themeResId = R.style.Theme_Catok){DataProdukFragment().also {fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever{viewLifecycleOwner ->
                if(viewLifecycleOwner!=null){
                    navController.setGraph(R.navigation.catok_nav_graph)
                    Navigation.setViewNavController(fragment.requireView(),navController)
                }
            }
        }}
//        val mockNavController = mock(NavController::class.java)
//
//        val titleScenario = launchFragmentInContainer<DataProdukFragment>(themeResId = R.style.Theme_Catok)
//
//        titleScenario.onFragment{fragment->
//            fragment.viewLifecycleOwnerLiveData.observeForever{viewLifecycleOwner ->
//                if(viewLifecycleOwner!=null){
//                    mockNavController.setGraph(R.navigation.catok_nav_graph)
//                    Navigation.setViewNavController(fragment.requireView(),mockNavController)
//                }
//            }
//        }
        onView(withId(R.id.recyclerViewDataProduk)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, longClick()))
        onView(withText("Select All")).perform(click())
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
        onView(withId(R.id.recyclerViewDataProduk)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, longClick()))
    }

    class ClickOnButtonView(private val id: Int) : ViewAction {
        private var click = click()

        override fun getConstraints(): Matcher<View> {
            return click.constraints
        }

        override fun getDescription(): String {
            return " click on custom button view"
        }

        override fun perform(uiController: UiController, view: View) {
            //btnClickMe -> Custom row item view button
            click.perform(uiController, view.findViewById(id))
        }
    }
