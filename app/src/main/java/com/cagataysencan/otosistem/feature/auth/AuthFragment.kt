package com.cagataysencan.otosistem.feature.auth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cagataysencan.otosistem.R
import com.cagataysencan.otosistem.core.base.BaseBindingFragment
import com.cagataysencan.otosistem.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Sign-in screen. Shows loading dialog during auth and navigates on success.
 */
@AndroidEntryPoint
class AuthFragment : BaseBindingFragment<FragmentAuthBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    /** Inflates the auth layout binding. */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAuthBinding {
        return FragmentAuthBinding.inflate(inflater, container, false)
    }

    /** Wires the sign-in button and observes navigation events from the ViewModel. */
    override fun onInit(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        binding.btnSignIn.setOnClickListener {
            showLoadingDialog()
            viewModel.onSignInClicked()
        }

        viewModel.navigationEvent.collectOnStarted { event ->
            hideLoadingDialog()
            when (event) {
                AuthViewModel.AuthNavigationEvent.NavigateToHome -> {
                    findNavController().navigate(R.id.action_auth_to_home)
                }
            }
        }
    }
}
