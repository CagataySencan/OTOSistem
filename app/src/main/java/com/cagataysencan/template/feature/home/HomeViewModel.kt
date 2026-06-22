package com.cagataysencan.template.feature.home

import com.cagataysencan.template.R
import com.cagataysencan.template.core.base.BaseViewModel
import com.cagataysencan.template.core.common.ResourceProvider
import com.cagataysencan.template.core.common.ResultState
import com.cagataysencan.template.core.manager.feature.FeatureFlag
import com.cagataysencan.template.core.manager.feature.FeatureFlagManager
import com.cagataysencan.template.core.network.monitor.NetworkMonitor
import com.cagataysencan.template.di.DispatchersProvider
import com.cagataysencan.template.domain.model.Post
import com.cagataysencan.template.domain.usecase.GetPostsUseCase
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
