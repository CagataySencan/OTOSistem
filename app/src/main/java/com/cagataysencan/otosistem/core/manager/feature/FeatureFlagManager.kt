package com.cagataysencan.otosistem.core.manager.feature

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.cagataysencan.otosistem.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

enum class FeatureFlag {
    SETTINGS,
}

/**
 * Controls feature toggles. Check in ViewModels/fragments before showing optional screens or actions.
 */
interface FeatureFlagManager {

    /** Returns whether [flag] is enabled; caches the result after first read. */
    fun isEnabled(flag: FeatureFlag): Boolean

    /** Observes [flag] changes over time from DataStore. */
    fun observe(flag: FeatureFlag): Flow<Boolean>

    /** Persists the enabled state for [flag]. */
    suspend fun setEnabled(flag: FeatureFlag, enabled: Boolean)
}

@Singleton
class FeatureFlagManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : FeatureFlagManager {

    private val cache = mutableMapOf<FeatureFlag, Boolean>()

    /** Reads from cache or DataStore once; suitable for init-time checks in ViewModels. */
    override fun isEnabled(flag: FeatureFlag): Boolean {
        return cache[flag] ?: runBlocking {
            observe(flag).first()
        }.also { cache[flag] = it }
    }

    /** Maps the DataStore preference to a boolean Flow for the given flag. */
    override fun observe(flag: FeatureFlag): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[flag.toKey()] ?: flag.defaultValue()
        }
    }

    /** Writes the flag state to DataStore and updates the in-memory cache. */
    override suspend fun setEnabled(flag: FeatureFlag, enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[flag.toKey()] = enabled
        }
        cache[flag] = enabled
    }

    /** Returns the build-time default when no override is stored. */
    private fun FeatureFlag.defaultValue(): Boolean {
        return when (this) {
            FeatureFlag.SETTINGS -> BuildConfig.FEATURE_SETTINGS_ENABLED
        }
    }

    /** Builds the DataStore key for this flag. */
    private fun FeatureFlag.toKey(): Preferences.Key<Boolean> {
        return booleanPreferencesKey("feature_${name.lowercase()}")
    }
}
