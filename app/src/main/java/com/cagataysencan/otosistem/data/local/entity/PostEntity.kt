package com.cagataysencan.otosistem.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room table model for cached posts. Mapped to [com.cagataysencan.otosistem.domain.model.Post]
 * via [com.cagataysencan.otosistem.data.mapper.PostEntityMapper].
 */
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)
