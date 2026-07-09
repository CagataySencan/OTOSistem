package com.cagataysencan.otosistem.data.mapper

import com.cagataysencan.otosistem.data.local.entity.PostEntity
import com.cagataysencan.otosistem.domain.model.Post
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Maps between Room [PostEntity] and domain [Post]. Used by [PostLocalDataSource].
 */
@Singleton
class PostEntityMapper @Inject constructor() {

    /** Converts a Room entity to the domain model. */
    fun toDomain(from: PostEntity): Post {
        return Post(
            id = from.id,
            userId = from.userId,
            title = from.title,
            body = from.body,
        )
    }

    /** Converts a domain model to a Room entity for persistence. */
    fun toEntity(from: Post): PostEntity {
        return PostEntity(
            id = from.id,
            userId = from.userId,
            title = from.title,
            body = from.body,
        )
    }

    /** Converts a list of Room entities to domain models. */
    fun toDomainList(from: List<PostEntity>): List<Post> {
        return from.map(::toDomain)
    }

    /** Converts a list of domain models to Room entities. */
    fun toEntityList(from: List<Post>): List<PostEntity> {
        return from.map(::toEntity)
    }
}
