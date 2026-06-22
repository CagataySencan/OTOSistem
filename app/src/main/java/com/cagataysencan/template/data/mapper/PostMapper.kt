package com.cagataysencan.template.data.mapper

import com.cagataysencan.template.data.remote.dto.PostDto
import com.cagataysencan.template.domain.model.Post
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
