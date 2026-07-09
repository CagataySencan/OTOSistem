package com.cagataysencan.otosistem.domain.usecase

import com.cagataysencan.otosistem.domain.model.Post
import com.cagataysencan.otosistem.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Retrieves posts for the home screen. One use case per business action; inject into ViewModels.
 */
class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {

    /** Invokes the use case; call from ViewModels as getPostsUseCase(). */
    operator fun invoke(): Flow<List<Post>> = postRepository.getPosts()
}
