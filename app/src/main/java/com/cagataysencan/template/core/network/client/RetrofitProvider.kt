package com.cagataysencan.template.core.network.client

import com.cagataysencan.template.BuildConfig
import com.cagataysencan.template.data.remote.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Builds the singleton [Retrofit] instance and exposes [ApiService].
 * Provided to Hilt via [com.cagataysencan.template.di.module.AppModule].
 */
@Singleton
class RetrofitProvider @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** Returns the configured Retrofit instance for custom API creation. */
    fun provideRetrofit(): Retrofit = retrofit

    /** Creates and returns the typed [ApiService] proxy. */
    fun provideApiService(): ApiService = retrofit.create(ApiService::class.java)
}
