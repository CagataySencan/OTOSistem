package com.cagataysencan.otosistem.data.remote.dto

/**
 * API response model for a post. Mapped to [com.cagataysencan.otosistem.domain.model.Post] via [PostMapper].
 */
data class PostDto(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)
