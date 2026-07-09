package com.cagataysencan.otosistem.data.mapper

import com.cagataysencan.otosistem.data.remote.dto.PostDto
import com.cagataysencan.otosistem.domain.model.Post
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Maps [PostDto] to domain [Post]. Add mappers here for each new API entity type.
 */
@Singleton
class PostMapper @Inject constructor() {

    /** Converts an API DTO to the domain model used by use cases and UI. */
    fun map(from: PostDto): Post {
        return Post(
            id = from.id,
            userId = from.userId,
            title = from.title,
            body = from.body,
        )
    }
}
