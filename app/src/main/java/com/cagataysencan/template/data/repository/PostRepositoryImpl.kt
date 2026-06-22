package com.cagataysencan.template.data.repository

import com.cagataysencan.template.R
import com.cagataysencan.template.core.common.ResourceProvider
import com.cagataysencan.template.core.network.result.NetworkErrorKind
import com.cagataysencan.template.core.network.result.NetworkResponse
import com.cagataysencan.template.data.mapper.PostMapper
import com.cagataysencan.template.data.remote.source.PostRemoteDataSource
import com.cagataysencan.template.di.DispatchersProvider
import com.cagataysencan.template.domain.model.Post
import com.cagataysencan.template.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data-layer implementation of [PostRepository]. Coordinates remote sources and mappers.
 */
@Singleton
class PostRepositoryImpl @Inject constructor(
    private val remoteDataSource: PostRemoteDataSource,
    private val postMapper: PostMapper,
    private val dispatchers: DispatchersProvider,
    private val resourceProvider: ResourceProvider,
) : PostRepository {

    /** Fetches posts from the remote source, maps them to domain models, and emits on IO. */
    override fun getPosts(): Flow<List<Post>> = flow {
        when (val response = remoteDataSource.getPosts()) {
            is NetworkResponse.Success -> emit(response.data.map(postMapper::map))
            is NetworkResponse.Error -> {
                throw IllegalStateException(response.resolveMessage())
            }
        }
    }.flowOn(dispatchers.io)

    private fun NetworkResponse.Error.resolveMessage(): String {
        message?.let { return it }
        return when (kind) {
            NetworkErrorKind.NETWORK -> resourceProvider.getString(R.string.error_network)
            NetworkErrorKind.UNKNOWN -> resourceProvider.getString(R.string.error_unknown)
            null -> resourceProvider.getString(R.string.error_load_posts)
        }
    }
}
