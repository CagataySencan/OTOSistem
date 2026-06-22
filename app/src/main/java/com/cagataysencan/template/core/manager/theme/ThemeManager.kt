package com.cagataysencan.template.core.manager.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

enum class AppTheme {
    SYSTEM,
    LIGHT,
    DARK,
}

/**
 * Manages light/dark theme. Applied on startup in [MainApplication]; change from settings screens.
 */
interface ThemeManager {

    /** Emits the currently selected theme preference. */
    val currentTheme: Flow<AppTheme>

    /** Saves the theme preference and applies it immediately. */
    suspend fun setTheme(theme: AppTheme)

    /** Reads and applies the saved theme; call once on app startup. */
    fun applySavedTheme()
}

@Singleton
class ThemeManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : ThemeManager {

    /** Maps the stored theme name to [AppTheme]; defaults to SYSTEM. */
    override val currentTheme: Flow<AppTheme> = dataStore.data.map { preferences ->
        preferences[KEY_THEME]?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
            ?: AppTheme.SYSTEM
    }

    /** Persists the theme and updates AppCompatDelegate night mode. */
    override suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[KEY_THEME] = theme.name
        }
        applyTheme(theme)
    }

    /** Loads the saved theme synchronously and applies it on the main thread. */
    override fun applySavedTheme() {
        applyTheme(runBlocking { currentTheme.first() })
    }

    /** Sets AppCompatDelegate night mode based on the given theme. */
    private fun applyTheme(theme: AppTheme) {
        val mode = when (theme) {
            AppTheme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    companion object {
        private val KEY_THEME = stringPreferencesKey("app_theme")
    }
}
