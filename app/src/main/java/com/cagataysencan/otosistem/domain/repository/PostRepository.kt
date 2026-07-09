package com.cagataysencan.otosistem.domain.repository

import com.cagataysencan.otosistem.domain.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * Domain contract for post data. Implemented in the data layer; consumed by use cases.
 */
interface PostRepository {

    /** Returns a Flow that emits the list of posts when fetched. */
    fun getPosts(): Flow<List<Post>>
}
