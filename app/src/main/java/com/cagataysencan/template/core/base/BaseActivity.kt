package com.cagataysencan.template.core.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cagataysencan.template.di.LocalizationEntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors

/**
 * Base activity that applies saved locale and exposes an [onInit] hook.
 * Extend for every activity; annotate concrete classes with [@AndroidEntryPoint][AndroidEntryPoint].
 */
@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    /** Wraps the base context with the user's saved locale before the activity is created. */
    override fun attachBaseContext(newBase: Context) {
        val localizationManager = EntryPointAccessors.fromApplication(
            newBase.applicationContext,
            LocalizationEntryPoint::class.java,
        ).localizationManager()
        super.attachBaseContext(localizationManager.wrapContext(newBase))
    }

    /** Delegates setup to [onInit] so subclasses avoid overriding onCreate boilerplate. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInit(savedInstanceState)
    }

    /** Override to inflate views and configure the activity after creation. */
    protected open fun onInit(savedInstanceState: Bundle?) = Unit
}
