package com.cagataysencan.template.data.remote.dto

/**
 * API response model for a post. Mapped to [com.cagataysencan.template.domain.model.Post] via [PostMapper].
 */
data class PostDto(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)
