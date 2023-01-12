package org.d3ifcool.catok

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3ifcool.catok.ui.main.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationFragmentTest {
    @get:Rule
    val activityScenario = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        activityScenario.scenario.onActivity {
            it.supportFragmentManager.beginTransaction()
        }
    }

    @Test
    fun testGrafikNavigation(){
        onView(ViewMatchers.withId(R.id.grafikFragment))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(click())
    }
//
//    @Test
//    fun testNavigation(){
//        val mockNavController = Mockito.mock(NavController::class.java)
//
//        val titleScenario = launchFragmentInContainer<BerandaFragment>(themeResId = R.style.Theme_Catok)
//
//        titleScenario.onFragment{fragment->
//            fragment.viewLifecycleOwnerLiveData.observeForever{viewLifecycleOwner ->
//                if(viewLifecycleOwner!=null){
//                    mockNavController.setGraph(R.navigation.catok_nav_graph)
//                    Navigation.setViewNavController(fragment.requireView(),mockNavController)
//                }
//            }
//        }
//
//        onView(ViewMatchers.withId(R.id.dataProduk)).perform(click())
//        verify(mockNavController).navigate(R.id.action_berandaFragment_to_dataProdukFragment)
//
//        pressBack()
//
//        onView(ViewMatchers.withId(R.id.transaksiProduk)).perform(click())
//        verify(mockNavController).navigate(R.id.action_berandaFragment_to_transaksiFragment)
//    }
}