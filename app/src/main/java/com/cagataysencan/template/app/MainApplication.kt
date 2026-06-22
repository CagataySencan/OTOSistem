package com.cagataysencan.template.app

import android.app.Application
import com.cagataysencan.template.BuildConfig
import com.cagataysencan.template.core.manager.theme.ThemeManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * Application entry point. Initializes Hilt, Timber logging, and saved app theme.
 * Registered in AndroidManifest via android:name.
 */
@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    lateinit var themeManager: ThemeManager

    /** Initializes logging and applies the user's saved theme preference. */
    override fun onCreate() {
        super.onCreate()
        initLogging()
        themeManager.applySavedTheme()
    }

    /** Plants a debug Timber tree in debug builds; no-op tree in release. */
    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(object : Timber.Tree() {
                /** Suppresses all log output in release builds. */
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) = Unit
            })
        }
    }
}
