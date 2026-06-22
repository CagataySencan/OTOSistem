package com.cagataysencan.template.core.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.cagataysencan.template.R

/**
 * Non-cancelable loading dialog. Call via [BaseFragment.showLoadingDialog] / [hideLoadingDialog].
 */
class LoadingDialog : DialogFragment() {

    /** Builds a non-cancelable dialog with the loading layout. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Theme_MainTemplate_Dialog).apply {
            setContentView(R.layout.dialog_loading)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    companion object {
        private const val TAG = "LoadingDialog"

        /** Shows the dialog if not already visible; safe to call multiple times. */
        fun show(fragmentManager: FragmentManager) {
            if (fragmentManager.findFragmentByTag(TAG) != null) return
            LoadingDialog().show(fragmentManager, TAG)
        }

        /** Dismisses the dialog if it is currently shown. */
        fun dismiss(fragmentManager: FragmentManager) {
            (fragmentManager.findFragmentByTag(TAG) as? LoadingDialog)?.dismissAllowingStateLoss()
        }
    }
}
