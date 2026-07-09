package com.cagataysencan.otosistem.domain.model

/**
 * Domain model for a post. Used by use cases and UI; independent of API structure.
 */
data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)
