package org.d3ifcool.catok

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3ifcool.catok.ui.beranda.transaksi.TransaksiAdapter
import org.d3ifcool.catok.ui.main.MainActivity
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransaksiTest {

    @get:Rule
    val activityScenario = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Before
    fun init() {
        activityScenario.scenario.onActivity {
            it.supportFragmentManager.beginTransaction()
        }
    }

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