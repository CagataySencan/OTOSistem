package com.cagataysencan.otosistem.core.manager.localization

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages app language. Applied in [BaseActivity.attachBaseContext]; change from settings screens.
 */
interface LocalizationManager {

    /** Emits the current language code (e.g. "en", "tr"). */
    val currentLanguage: Flow<String>

    /** Saves the language preference and updates the default locale. */
    suspend fun setLanguage(languageCode: String)

    /** Returns a context configured with the saved locale; call in attachBaseContext. */
    fun wrapContext(base: Context): Context
}

@Singleton
class LocalizationManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : LocalizationManager {

    /** Maps the stored language code to a Flow; defaults to "en". */
    override val currentLanguage: Flow<String> = dataStore.data.map { preferences ->
        preferences[KEY_LANGUAGE] ?: DEFAULT_LANGUAGE
    }

    /** Persists the language code and sets Locale.setDefault. */
    override suspend fun setLanguage(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = languageCode
        }
        Locale.setDefault(Locale.forLanguageTag(languageCode))
    }

    /** Creates a configuration context with the saved locale applied. */
    override fun wrapContext(base: Context): Context {
        val language = runBlocking { currentLanguage.first() }
        val locale = Locale.forLanguageTag(language)
        Locale.setDefault(locale)

        val config = base.resources.configuration
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }

    companion object {
        private val KEY_LANGUAGE = stringPreferencesKey("app_language")
        private const val DEFAULT_LANGUAGE = "en"
    }
}
