package com.cagataysencan.otosistem.core.network.client

import com.cagataysencan.otosistem.core.network.interceptor.ChuckerInterceptorProvider
import com.cagataysencan.otosistem.core.network.interceptor.LoggingInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Builds the singleton [OkHttpClient] with timeouts, Chucker, and logging interceptors.
 * Provided to Hilt via [com.cagataysencan.otosistem.di.module.AppModule].
 */
@Singleton
class OkHttpClientProvider @Inject constructor(
    private val loggingInterceptor: LoggingInterceptor,
    private val chuckerInterceptorProvider: ChuckerInterceptorProvider,
) {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(chuckerInterceptorProvider.provide())
            .addInterceptor(loggingInterceptor.provide())
            .build()
    }

    /** Returns the configured singleton OkHttpClient instance. */
    fun provide(): OkHttpClient = client

    companion object {
        private const val TIMEOUT_SECONDS = 30L
    }
}
