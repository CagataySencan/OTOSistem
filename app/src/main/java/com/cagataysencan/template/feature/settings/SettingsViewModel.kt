package com.cagataysencan.template.feature.settings

import com.cagataysencan.template.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Settings screen state holder. Extend when adding theme, locale, or preference logic.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor() : BaseViewModel()
