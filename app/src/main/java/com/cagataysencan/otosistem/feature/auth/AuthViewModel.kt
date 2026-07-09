package com.cagataysencan.otosistem.feature.auth

import androidx.lifecycle.viewModelScope
import com.cagataysencan.otosistem.core.base.BaseViewModel
import com.cagataysencan.otosistem.core.manager.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Handles sign-in action and persists session before navigating to home.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sessionManager: SessionManager,
) : BaseViewModel() {

    private val navigationChannel = Channel<AuthNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = navigationChannel.receiveAsFlow()

    /** Marks the user as logged in and emits a navigation event to home. */
    fun onSignInClicked() {
        viewModelScope.launch {
            sessionManager.setLoggedIn(true)
            navigationChannel.send(AuthNavigationEvent.NavigateToHome)
        }
    }

    sealed interface AuthNavigationEvent {
        /** Sign-in succeeded; navigate to the home screen. */
        data object NavigateToHome : AuthNavigationEvent
    }
}
