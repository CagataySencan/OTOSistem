package com.cagataysencan.otosistem.core.common

import android.content.Context
import androidx.annotation.StringRes
import com.cagataysencan.otosistem.core.manager.localization.LocalizationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localizationManager: LocalizationManager,
) : ResourceProvider {

    override fun getString(@StringRes resId: Int): String {
        return localizedContext().getString(resId)
    }

    override fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return localizedContext().getString(resId, *formatArgs)
    }

    private fun localizedContext(): Context = localizationManager.wrapContext(context)
}
