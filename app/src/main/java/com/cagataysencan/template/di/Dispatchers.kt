package com.cagataysencan.template.di

import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Hilt qualifiers and provider for coroutine dispatchers. Inject [DispatchersProvider] in ViewModels.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

/** Abstraction over coroutine dispatchers for testability. */
interface DispatchersProvider {
    /** Main thread dispatcher for UI updates. */
    val main: CoroutineDispatcher

    /** IO dispatcher for network and disk operations. */
    val io: CoroutineDispatcher

    /** Default dispatcher for CPU-bound work. */
    val default: CoroutineDispatcher
}

@Singleton
class DefaultDispatchersProvider @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : DispatchersProvider {

    /** Returns the injected main thread dispatcher. */
    override val main: CoroutineDispatcher
        get() = mainDispatcher

    /** Returns the injected IO dispatcher. */
    override val io: CoroutineDispatcher
        get() = ioDispatcher

    /** Returns the injected default dispatcher. */
    override val default: CoroutineDispatcher
        get() = defaultDispatcher
}
