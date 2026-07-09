package com.cagataysencan.otosistem.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cagataysencan.otosistem.core.ui.adapter.BaseListAdapter
import com.cagataysencan.otosistem.databinding.ItemPostBinding
import com.cagataysencan.otosistem.domain.model.Post

/**
 * Displays [Post] items on the home screen RecyclerView.
 */
class PostAdapter : BaseListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    /** Creates a ViewHolder with an inflated [ItemPostBinding]. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return PostViewHolder(binding)
    }

    /** Binds the post at [position] to the ViewHolder. */
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PostViewHolder(
        private val binding: ItemPostBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        /** Populates title and body text from the given [post]. */
        fun bind(post: Post) {
            binding.textTitle.text = post.title
            binding.textBody.text = post.body
        }
    }

    private class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        /** Compares posts by id to detect item moves and insertions. */
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        /** Compares full post content to detect item updates. */
        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}
