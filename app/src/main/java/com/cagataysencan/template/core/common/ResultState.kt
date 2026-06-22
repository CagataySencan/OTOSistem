package com.cagataysencan.template.core.common

/**
 * UI state wrapper for async operations. Collect in fragments to drive loading, content, and error UI.
 */
sealed interface ResultState<out T> {

    /** Initial state before any request has been made. */
    data object Idle : ResultState<Nothing>

    /** Request is in progress; show a loading indicator. */
    data object Loading : ResultState<Nothing>

    /** Request succeeded; render [data] in the UI. */
    data class Success<T>(val data: T) : ResultState<T>

    /** Request failed; show [message] and optionally log [throwable]. */
    data class Error(
        val message: String,
        val throwable: Throwable? = null,
    ) : ResultState<Nothing>
}
