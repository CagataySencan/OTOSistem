package com.cagataysencan.otosistem.core.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * ViewBinding-enabled bottom sheet. Implement [inflateBinding]; annotate subclasses with [@AndroidEntryPoint][dagger.hilt.android.AndroidEntryPoint].
 */
abstract class BaseBottomSheetDialogFragment<VB : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    /** Inflates the bottom sheet layout binding; called once in [onCreateView]. */
    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    /** Creates the bottom sheet view from the inflated binding. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    /** Delegates setup to [onInit] after the view hierarchy is created. */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInit(view, savedInstanceState)
    }

    /** Clears the binding reference to prevent leaks when the view is destroyed. */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Override to wire click listeners and observers after the view is created. */
    protected open fun onInit(view: View, savedInstanceState: Bundle?) = Unit
}
