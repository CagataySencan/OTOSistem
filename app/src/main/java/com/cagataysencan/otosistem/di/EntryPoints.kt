package com.cagataysencan.otosistem.di

import com.cagataysencan.otosistem.core.manager.localization.LocalizationManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt entry points for dependencies needed before field injection (e.g. in attachBaseContext).
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocalizationEntryPoint {

    /** Returns the singleton [LocalizationManager] for use outside Hilt-injected classes. */
    fun localizationManager(): LocalizationManager
}
