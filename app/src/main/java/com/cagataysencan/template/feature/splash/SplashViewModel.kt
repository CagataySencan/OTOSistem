package com.cagataysencan.template.feature.splash

import androidx.lifecycle.viewModelScope
import com.cagataysencan.template.core.base.BaseViewModel
import com.cagataysencan.template.core.manager.session.SessionManager
import com.cagataysencan.template.di.DispatchersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Checks session on startup and emits a one-time navigation event to auth or home.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val dispatchers: DispatchersProvider,
) : BaseViewModel() {

    private val navigationChannel = Channel<SplashNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = navigationChannel.receiveAsFlow()

    /** Waits for splash delay, then routes to home or auth based on session state. */
    init {
        viewModelScope.launch(dispatchers.io) {
            delay(SPLASH_DELAY_MS)
            val destination = if (sessionManager.isLoggedInSnapshot()) {
                SplashNavigationEvent.NavigateToHome
            } else {
                SplashNavigationEvent.NavigateToAuth
            }
            navigationChannel.send(destination)
        }
    }

    sealed interface SplashNavigationEvent {
        /** User is not logged in; navigate to the auth screen. */
        data object NavigateToAuth : SplashNavigationEvent

        /** User is logged in; navigate directly to home. */
        data object NavigateToHome : SplashNavigationEvent
    }

    companion object {
        private const val SPLASH_DELAY_MS = 2_000L
    }
}
