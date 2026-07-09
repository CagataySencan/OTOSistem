package com.cagataysencan.otosistem.core.network.monitor

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Observes device connectivity. Inject into ViewModels to gate network requests or show offline UI.
 */
interface NetworkMonitor {

    /** Emits true when the device has internet access; false when offline. */
    val isOnline: Flow<Boolean>

    /** Returns the current connectivity status without observing changes. */
    fun isCurrentlyOnline(): Boolean
}

@Singleton
class NetworkMonitorImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : NetworkMonitor {

    private val connectivityManager: ConnectivityManager
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /** Registers a network callback and emits connectivity changes until the Flow is cancelled. */
    override val isOnline: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            /** Notifies subscribers when a network becomes available. */
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            /** Notifies subscribers when a network connection is lost. */
            override fun onLost(network: Network) {
                trySend(false)
            }

            /** Re-evaluates connectivity when network capabilities change. */
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities,
            ) {
                trySend(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)
        trySend(isCurrentlyOnline())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

    /** Checks the active network for internet capability; returns false if none is active. */
    override fun isCurrentlyOnline(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
