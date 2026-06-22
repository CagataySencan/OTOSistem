package com.cagataysencan.template.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewBinding-enabled fragment with lifecycle-aware Flow collection.
 * Implement [inflateBinding] and override [onInit] to set up UI and observers.
 */
abstract class BaseBindingFragment<VB : ViewBinding> : BaseFragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    /** Inflates the feature layout binding; called once in [onCreateView]. */
    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    /** Creates the fragment view from the inflated binding. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    /** Clears the binding reference to prevent leaks when the view is destroyed. */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Collects this Flow only while the fragment is at least STARTED; use for ViewModel state. */
    protected fun <T> Flow<T>.collectOnStarted(action: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect(action)
            }
        }
    }
}
