package com.odc.odctrackingcommercial.lib.depot

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.odc.odctrackingcommercial.lib.utils.Constantes
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constantes.PREF_NAME)

class DataStoreDepot @Inject constructor(@ApplicationContext private val context: Context) {
    private object PreferenceKeys {
        val userKey = stringPreferencesKey(Constantes.PREF_KEY_USER)
        val identifiantKey = stringPreferencesKey(Constantes.PREF_KEY_IDENTIFIANT)
    }

    private val dataStore = context.dataStore
    suspend fun persisteUserState(name: String) {
        dataStore.edit { pref ->
            pref[PreferenceKeys.userKey] = name
        }
    }

    suspend fun persisteIdentifiantState(name: String) {
        dataStore.edit { pref ->
            pref[PreferenceKeys.identifiantKey] = name
        }
    }

    val readUserState: Flow<String> = dataStore.data.catch { exc ->
        if (exc is IOException) {
            emit(emptyPreferences())
        } else {
            throw exc
        }

    }.map { pref ->
        val userState = pref[PreferenceKeys.userKey] ?: ""
        userState
    }

    val readIdentifiantState: Flow<String> = dataStore.data.catch { exc ->
        if (exc is IOException) {
            emit(emptyPreferences())
        } else {
            throw exc
        }

    }.map { pref ->
        val state = pref[PreferenceKeys.identifiantKey] ?: ""
        state
    }
}