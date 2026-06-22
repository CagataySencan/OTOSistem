package com.cagataysencan.template.di.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.cagataysencan.template.core.common.AndroidResourceProvider
import com.cagataysencan.template.core.common.ResourceProvider
import com.cagataysencan.template.core.manager.feature.FeatureFlagManager
import com.cagataysencan.template.core.manager.feature.FeatureFlagManagerImpl
import com.cagataysencan.template.core.manager.localization.LocalizationManager
import com.cagataysencan.template.core.manager.localization.LocalizationManagerImpl
import com.cagataysencan.template.core.manager.session.SessionManager
import com.cagataysencan.template.core.manager.session.SessionManagerImpl
import com.cagataysencan.template.core.manager.theme.ThemeManager
import com.cagataysencan.template.core.manager.theme.ThemeManagerImpl
import com.cagataysencan.template.core.network.client.OkHttpClientProvider
import com.cagataysencan.template.core.network.client.RetrofitProvider
import com.cagataysencan.template.core.network.monitor.NetworkMonitor
import com.cagataysencan.template.core.network.monitor.NetworkMonitorImpl
import com.cagataysencan.template.data.remote.api.ApiService
import com.cagataysencan.template.data.repository.PostRepositoryImpl
import com.cagataysencan.template.di.DefaultDispatcher
import com.cagataysencan.template.di.DefaultDispatchersProvider
import com.cagataysencan.template.di.DispatchersProvider
import com.cagataysencan.template.di.IoDispatcher
import com.cagataysencan.template.di.MainDispatcher
import com.cagataysencan.template.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Central Hilt module. Binds managers, repositories, and provides network/DataStore dependencies.
 * Room is provided via lazy [com.cagataysencan.template.data.local.database.DatabaseProvider]
 * and is not initialized on application startup.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /** Binds session persistence to [SessionManagerImpl]. */
    @Binds
    @Singleton
    abstract fun bindSessionManager(impl: SessionManagerImpl): SessionManager

    /** Binds theme management to [ThemeManagerImpl]. */
    @Binds
    @Singleton
    abstract fun bindThemeManager(impl: ThemeManagerImpl): ThemeManager

    /** Binds locale management to [LocalizationManagerImpl]. */
    @Binds
    @Singleton
    abstract fun bindLocalizationManager(impl: LocalizationManagerImpl): LocalizationManager

    /** Binds feature flag management to [FeatureFlagManagerImpl]. */
    @Binds
    @Singleton
    abstract fun bindFeatureFlagManager(impl: FeatureFlagManagerImpl): FeatureFlagManager

    /** Binds network connectivity monitoring to [NetworkMonitorImpl]. */
    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(impl: NetworkMonitorImpl): NetworkMonitor

    /** Binds post data access to [PostRepositoryImpl]. */
    @Binds
    @Singleton
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    /** Binds localized string resolution to [AndroidResourceProvider]. */
    @Binds
    @Singleton
    abstract fun bindResourceProvider(impl: AndroidResourceProvider): ResourceProvider

    /** Binds coroutine dispatcher access to [DefaultDispatchersProvider]. */
    @Binds
    @Singleton
    abstract fun bindDispatchersProvider(impl: DefaultDispatchersProvider): DispatchersProvider

    companion object {

        /** Provides the shared DataStore for session, theme, locale, and feature preferences. */
        @Provides
        @Singleton
        fun provideDataStore(
            @ApplicationContext context: Context,
        ): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create {
                context.preferencesDataStoreFile(DATA_STORE_NAME)
            }
        }

        /** Provides the configured OkHttpClient from [OkHttpClientProvider]. */
        @Provides
        @Singleton
        fun provideOkHttpClient(
            okHttpClientProvider: OkHttpClientProvider,
        ): OkHttpClient = okHttpClientProvider.provide()

        /** Provides the configured Retrofit instance from [RetrofitProvider]. */
        @Provides
        @Singleton
        fun provideRetrofit(
            retrofitProvider: RetrofitProvider,
        ): Retrofit = retrofitProvider.provideRetrofit()

        /** Provides the Retrofit [ApiService] proxy from [RetrofitProvider]. */
        @Provides
        @Singleton
        fun provideApiService(
            retrofitProvider: RetrofitProvider,
        ): ApiService = retrofitProvider.provideApiService()

        /** Provides the main thread dispatcher for UI coroutines. */
        @Provides
        @Singleton
        @MainDispatcher
        fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

        /** Provides the IO dispatcher for network and disk operations. */
        @Provides
        @Singleton
        @IoDispatcher
        fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

        /** Provides the default dispatcher for CPU-bound work. */
        @Provides
        @Singleton
        @DefaultDispatcher
        fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

        private const val DATA_STORE_NAME = "app_preferences"
    }
}
