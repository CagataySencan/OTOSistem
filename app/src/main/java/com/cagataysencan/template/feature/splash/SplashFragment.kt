package com.cagataysencan.template.feature.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cagataysencan.template.R
import com.cagataysencan.template.core.base.BaseBindingFragment
import com.cagataysencan.template.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Startup screen. Observes [SplashViewModel.navigationEvent] and navigates to the next destination.
 */
@AndroidEntryPoint
class SplashFragment : BaseBindingFragment<FragmentSplashBinding>() {

    private val viewModel: SplashViewModel by viewModels()

    /** Inflates the splash layout binding. */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    /** Observes navigation events and triggers the appropriate NavController action. */
    override fun onInit(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        viewModel.navigationEvent.collectOnStarted { event ->
            when (event) {
                SplashViewModel.SplashNavigationEvent.NavigateToAuth -> {
                    findNavController().navigate(R.id.action_splash_to_auth)
                }

                SplashViewModel.SplashNavigationEvent.NavigateToHome -> {
                    findNavController().navigate(R.id.action_splash_to_home)
                }
            }
        }
    }
}
