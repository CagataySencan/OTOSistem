package com.cagataysencan.template.core.common

import androidx.annotation.StringRes

/**
 * Resolves string resources outside the UI layer (ViewModels, repositories).
 * Uses the saved app locale via [LocalizationManager].
 */
interface ResourceProvider {

    fun getString(@StringRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
}
