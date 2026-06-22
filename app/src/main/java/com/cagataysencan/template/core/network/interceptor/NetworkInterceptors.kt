package com.cagataysencan.template.core.network.interceptor

import com.cagataysencan.template.BuildConfig
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Timber-backed OkHttp logger for debug Logcat output. Added to [OkHttpClientProvider].
 */
@Singleton
class LoggingInterceptor @Inject constructor() {

    /** Builds the logging interceptor; BODY level in debug, NONE in release. */
    fun provide(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Timber.tag(LOG_TAG).d(message)
        }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    companion object {
        private const val LOG_TAG = "OkHttp"
    }
}

/**
 * Chucker HTTP inspector (debug UI; no-op in release). Added to [OkHttpClientProvider].
 */
@Singleton
class ChuckerInterceptorProvider @Inject constructor(
    @ApplicationContext context: android.content.Context,
) {

    private val interceptor: Interceptor = ChuckerInterceptor.Builder(context).build()

    /** Returns the Chucker interceptor for inspecting HTTP traffic in debug builds. */
    fun provide(): Interceptor = interceptor
}
