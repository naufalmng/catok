package org.d3ifcool.catok

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3ifcool.catok.ui.beranda.BerandaFragment
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class NavigationFragmentTest {
    @Test
    fun testNavigation(){
        val mockNavController = Mockito.mock(NavController::class.java)

        val titleScenario = launchFragmentInContainer<BerandaFragment>(themeResId = R.style.Theme_Catok)

        titleScenario.onFragment{fragment->
            fragment.viewLifecycleOwnerLiveData.observeForever{viewLifecycleOwner ->
                if(viewLifecycleOwner!=null){
                    mockNavController.setGraph(R.navigation.catok_nav_graph)
                    Navigation.setViewNavController(fragment.requireView(),mockNavController)
                }
            }
        }

        onView(ViewMatchers.withId(R.id.dataProduk)).perform(click())
        verify(mockNavController).navigate(R.id.action_berandaFragment_to_dataProdukFragment)

        pressBack()

        onView(ViewMatchers.withId(R.id.transaksiProduk)).perform(click())
        verify(mockNavController).navigate(R.id.action_berandaFragment_to_transaksiFragment)
    }
}