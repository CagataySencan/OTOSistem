package com.cagataysencan.otosistem.core.ui.dialog

import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.cagataysencan.otosistem.R

/**
 * Reusable error alert. Call via [BaseFragment.showErrorDialog] or directly from any [Fragment].
 */
object ErrorDialog {

    /**
     * Shows a Material alert with [message]; no-op if the fragment is not attached.
     * [onDismiss] runs when the user taps the positive button.
     */
    fun show(
        fragment: Fragment,
        message: String,
        title: String = fragment.getString(R.string.error_dialog_title),
        positiveButtonText: String = fragment.getString(R.string.error_dialog_ok),
        onDismiss: () -> Unit = {},
    ) {
        if (!fragment.isAdded) return

        MaterialAlertDialogBuilder(fragment.requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { dialog, _ ->
                dialog.dismiss()
                onDismiss()
            }
            .setCancelable(true)
            .show()
    }
}
