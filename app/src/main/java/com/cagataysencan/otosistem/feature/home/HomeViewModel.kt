package com.cagataysencan.otosistem.feature.home

import com.cagataysencan.otosistem.R
import com.cagataysencan.otosistem.core.base.BaseViewModel
import com.cagataysencan.otosistem.core.common.ResourceProvider
import com.cagataysencan.otosistem.core.common.ResultState
import com.cagataysencan.otosistem.core.manager.feature.FeatureFlag
import com.cagataysencan.otosistem.core.manager.feature.FeatureFlagManager
import com.cagataysencan.otosistem.core.network.monitor.NetworkMonitor
import com.cagataysencan.otosistem.di.DispatchersProvider
import com.cagataysencan.otosistem.domain.model.Post
import com.cagataysencan.otosistem.domain.usecase.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Loads and exposes post list state. Observed by [HomeFragment] via [uiState].
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val dispatchers: DispatchersProvider,
    private val networkMonitor: NetworkMonitor,
    private val resourceProvider: ResourceProvider,
    featureFlagManager: FeatureFlagManager,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<ResultState<List<Post>>>(ResultState.Idle)
    val uiState: StateFlow<ResultState<List<Post>>> = _uiState.asStateFlow()

    val settingsEnabled: Boolean = featureFlagManager.isEnabled(FeatureFlag.SETTINGS)

    /** Triggers the initial post load when the ViewModel is created. */
    init {
        loadPosts()
    }

    /** Checks connectivity, then fetches posts and updates [uiState]. Safe to call for pull-to-refresh. */
    fun loadPosts() {
        if (!networkMonitor.isCurrentlyOnline()) {
            _uiState.value = ResultState.Error(
                message = resourceProvider.getString(R.string.error_no_network),
            )
            return
        }

        _uiState.launchFlow(dispatchers, resourceProvider) {
            getPostsUseCase()
        }
    }
}
