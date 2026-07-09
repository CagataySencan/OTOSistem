package com.cagataysencan.otosistem.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cagataysencan.otosistem.R
import com.cagataysencan.otosistem.core.base.BaseBindingFragment
import com.cagataysencan.otosistem.core.common.ResultState
import com.cagataysencan.otosistem.databinding.FragmentHomeBinding
import com.cagataysencan.otosistem.domain.model.Post
import com.cagataysencan.otosistem.feature.home.adapter.PostAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main content screen. Shows post list and navigates to settings when the feature flag is enabled.
 */
@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private val postAdapter = PostAdapter()

    /** Inflates the home layout binding. */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    /** Sets up toolbar, RecyclerView, swipe-to-refresh, and observes [HomeViewModel.uiState]. */
    override fun onInit(view: View, savedInstanceState: android.os.Bundle?) {
        if (viewModel.settingsEnabled) {
            binding.toolbar.setNavigationIcon(R.drawable.ic_settings)
            binding.toolbar.navigationContentDescription = getString(R.string.nav_settings)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigate(R.id.action_home_to_settings)
            }
        } else {
            binding.toolbar.navigationIcon = null
        }

        binding.recyclerPosts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadPosts()
        }

        viewModel.uiState.collectOnStarted(::renderUiState)
    }

    /** Maps [ResultState] to visibility, adapter data, and error dialog display. */
    private fun renderUiState(state: ResultState<List<Post>>) {
        binding.progressBar.isVisible = state is ResultState.Loading && !binding.swipeRefresh.isRefreshing
        binding.swipeRefresh.isRefreshing = state is ResultState.Loading && postAdapter.itemCount > 0
        binding.textError.isVisible = state is ResultState.Error
        binding.recyclerPosts.isVisible = state is ResultState.Success

        when (state) {
            is ResultState.Error -> {
                binding.textError.text = state.message
                binding.swipeRefresh.isRefreshing = false
                showErrorDialog(state.message)
            }

            is ResultState.Success -> {
                postAdapter.submitList(state.data)
                binding.swipeRefresh.isRefreshing = false
            }

            else -> Unit
        }
    }
}
