package com.cagataysencan.otosistem.core.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.cagataysencan.otosistem.core.ui.dialog.ErrorDialog
import com.cagataysencan.otosistem.core.ui.dialog.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * Base fragment with loading and error dialog helpers.
 * Extend for simple fragments; use [BaseBindingFragment] when ViewBinding is needed.
 */
@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    /** Delegates setup to [onInit] after the view hierarchy is created. */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInit(view, savedInstanceState)
    }

    /** Override to wire click listeners, observers, and other UI logic. */
    protected open fun onInit(view: View, savedInstanceState: Bundle?) = Unit

    /** Shows a non-cancelable loading overlay; safe to call multiple times. */
    protected fun showLoadingDialog() {
        LoadingDialog.show(parentFragmentManager)
    }

    /** Dismisses the loading overlay if one is currently shown. */
    protected fun hideLoadingDialog() {
        LoadingDialog.dismiss(parentFragmentManager)
    }

    /** Shows a Material error alert with an optional dismiss callback. */
    protected fun showErrorDialog(
        message: String,
        onDismiss: () -> Unit = {},
    ) {
        ErrorDialog.show(this, message, onDismiss = onDismiss)
    }
}
