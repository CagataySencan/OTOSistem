package com.cagataysencan.otosistem.feature.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cagataysencan.otosistem.core.base.BaseBindingFragment
import com.cagataysencan.otosistem.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * App settings screen. Extend to wire [ThemeManager] and [LocalizationManager] controls.
 */
@AndroidEntryPoint
class SettingsFragment : BaseBindingFragment<FragmentSettingsBinding>() {

    private val viewModel: SettingsViewModel by viewModels()

    /** Inflates the settings layout binding. */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    /** Sets up the toolbar back navigation to return to the previous screen. */
    override fun onInit(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}
