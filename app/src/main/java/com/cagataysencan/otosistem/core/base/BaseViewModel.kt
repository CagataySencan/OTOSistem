package com.cagataysencan.otosistem.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cagataysencan.otosistem.R
import com.cagataysencan.otosistem.core.common.ResourceProvider
import com.cagataysencan.otosistem.core.common.ResultState
import com.cagataysencan.otosistem.di.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Base ViewModel with [launchFlow] helper for exposing [ResultState] from repository Flows.
 * Extend and inject use cases; expose [StateFlow] properties for the UI.
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * Collects a repository Flow on IO, updating this StateFlow through Loading → Success or Error.
     * Pass the target [_uiState] and a block returning the use case Flow.
     */
    protected fun <T> MutableStateFlow<ResultState<T>>.launchFlow(
        dispatchers: DispatchersProvider,
        resourceProvider: ResourceProvider,
        block: suspend () -> Flow<T>,
    ) {
        viewModelScope.launch {
            block()
                .flowOn(dispatchers.io)
                .onStart { value = ResultState.Loading }
                .catch { throwable ->
                    value = ResultState.Error(
                        message = throwable.message
                            ?: resourceProvider.getString(R.string.error_unknown),
                        throwable = throwable,
                    )
                }
                .collect { data ->
                    value = ResultState.Success(data)
                }
        }
    }

    /** Creates a mutable StateFlow with the given initial value. */
    protected fun <T> stateFlowOf(initial: T): MutableStateFlow<T> = MutableStateFlow(initial)

    /** Exposes a mutable StateFlow as read-only for the UI layer. */
    protected fun <T> MutableStateFlow<T>.asUiState(): StateFlow<T> = asStateFlow()
}
