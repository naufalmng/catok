package org.d3ifcool.catok.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

const val DATA_STORE_PREFERENCE = "DATA_STORE_PREFERENCE"
const val LAYOUT_MANAGER_PREFERENCES_KEY = "isLinearLayoutManager"
const val DATE_PREFERENCES_KEY = "currentDate"
const val MONTH_PREFERENCES_KEY = "currentMonth"
const val UPDATE_STATUS_PREFERENCES_KEY = "isDataUpdated"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_PREFERENCE)
class DataStorePreferences(preferencesDataStore: DataStore<Preferences>) {
    companion object{
        private val IS_LINEAR_LAYOUT_MANAGER = booleanPreferencesKey(LAYOUT_MANAGER_PREFERENCES_KEY)
        private val CURRENT_DATE = stringPreferencesKey(DATE_PREFERENCES_KEY)
        private val CURRENT_MONTH = stringPreferencesKey(MONTH_PREFERENCES_KEY)
        private val IS_DATA_UPDATED = booleanPreferencesKey(UPDATE_STATUS_PREFERENCES_KEY)
    }

    suspend fun saveLayoutSetting(ctx:Context,isLinearLayoutManager: Boolean){
        ctx.dataStore.edit { pref ->
            pref[IS_LINEAR_LAYOUT_MANAGER] = isLinearLayoutManager
        }
    }
    suspend fun saveDataUpdateSetting(ctx:Context,isDataUpdated: Boolean){
        ctx.dataStore.edit { pref ->
            pref[IS_DATA_UPDATED] = isDataUpdated
        }
    }
    suspend fun saveCurrentDate(ctx:Context, date: String){
        ctx.dataStore.edit { pref ->
            pref[CURRENT_DATE] = date
        }
    }
    suspend fun saveCurrentMonth(ctx:Context, month: String){
        ctx.dataStore.edit { pref ->
            pref[CURRENT_MONTH] = month
        }
    }

    val layoutPrefFlow: Flow<Boolean> = preferencesDataStore.data
        .catch {
            if(it is IOException) it.printStackTrace()
            else throw it
        }.map { pref ->
            pref[IS_LINEAR_LAYOUT_MANAGER]?:true
        }
    val isDataUpdated: Flow<Boolean> = preferencesDataStore.data
        .catch {
            if(it is IOException) it.printStackTrace()
            else throw it
        }.map { pref ->
            pref[IS_DATA_UPDATED]?:false
        }
    val currentDatePrefFlow: Flow<String?> = preferencesDataStore.data
        .catch {
            if(it is IOException) it.printStackTrace()
            else throw it
        }.map { pref ->
            pref[CURRENT_DATE]
        }
    val currentMonthPrefFlow: Flow<String?> = preferencesDataStore.data
        .catch {
            if(it is IOException) it.printStackTrace()
            else throw it
        }.map { pref ->
            pref[CURRENT_MONTH]
        }

}