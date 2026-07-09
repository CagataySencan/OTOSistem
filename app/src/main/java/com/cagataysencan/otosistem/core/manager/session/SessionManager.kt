package com.cagataysencan.otosistem.core.manager.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages login session state. Use in splash for routing and in auth to persist sign-in.
 */
interface SessionManager {

    /** Emits true when the user is logged in; false otherwise. */
    val isLoggedIn: Flow<Boolean>

    /** Persists the login state; call with true after successful sign-in. */
    suspend fun setLoggedIn(loggedIn: Boolean)

    /** Removes the stored session; call on sign-out. */
    suspend fun clearSession()

    /** Reads the current login state once; use in splash before navigation. */
    suspend fun isLoggedInSnapshot(): Boolean
}

@Singleton
class SessionManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SessionManager {

    /** Maps the DataStore preference to a login-state Flow. */
    override val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[KEY_LOGGED_IN] ?: false
    }

    /** Writes the login flag to DataStore. */
    override suspend fun setLoggedIn(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_LOGGED_IN] = loggedIn
        }
    }

    /** Clears the login flag from DataStore. */
    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_LOGGED_IN)
        }
    }

    /** Suspends until the first login value is available from DataStore. */
    override suspend fun isLoggedInSnapshot(): Boolean = isLoggedIn.first()

    companion object {
        private val KEY_LOGGED_IN = booleanPreferencesKey("session_logged_in")
    }
}
