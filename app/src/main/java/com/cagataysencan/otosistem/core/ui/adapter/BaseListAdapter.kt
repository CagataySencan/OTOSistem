package com.cagataysencan.otosistem.core.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Base [ListAdapter] with [DiffUtil]. Extend for feature-specific RecyclerView adapters.
 */
abstract class BaseListAdapter<T, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, VH>(diffCallback)
