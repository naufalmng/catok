package org.d3ifcool.catok

import android.Manifest
import android.os.Environment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import org.d3ifcool.catok.ui.main.MainActivity
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class PengaturanFragmentTest {

    @get:Rule
    val activityScenario = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Before
    fun init() {
        activityScenario.scenario.onActivity {
            it.supportFragmentManager.beginTransaction()
        }
    }

    @Test
    fun editProfil(){
        onView(withId(R.id.pengaturanFragment))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.editNama))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.etNamaToko))
            .inRoot(RootMatchers.isDialog())
            .perform(typeText("Toko Bagus"))
            .perform(closeSoftKeyboard())
            .check(matches(withText("Toko Bagus")))

        onView(withText("OK"))
            .perform(click())
    }

    @Test
    fun testBackupData(){
        lateinit var activity : MainActivity
        activityScenario.scenario.onActivity { activity = it }

        val folder = File("${Environment.getExternalStorageDirectory()}/BackupCatok")
        val csvFileName = "${activity.getString(R.string.nama_toko).replace("\\s".toRegex(), "")}_Backup.csv"
        val fileNameAndPath = "$folder/$csvFileName"

        onView(withId(R.id.pengaturanFragment))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.backupData))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText("Klik 1x lagi untuk backup data.."))
            .inRoot(withDecorView(not(activity.window.decorView)))
            .check(matches(isDisplayed()))

        onView(withId(R.id.backupData))
            .check(matches(isDisplayed()))
            .perform(click())


        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Backup Telah diexport ke $fileNameAndPath")))
    }

    @Test
    fun testRestoreData(){
        lateinit var activity : MainActivity
        activityScenario.scenario.onActivity { activity = it }

        onView(withId(R.id.pengaturanFragment))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.restoreData))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText("Data berhasil direstore..."))
            .inRoot(withDecorView(not(activity.window.decorView)))
            .check(matches(isDisplayed()))

    }
}