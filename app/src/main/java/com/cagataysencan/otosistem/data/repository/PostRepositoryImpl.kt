package com.cagataysencan.otosistem.data.repository

import com.cagataysencan.otosistem.R
import com.cagataysencan.otosistem.core.common.ResourceProvider
import com.cagataysencan.otosistem.core.network.result.NetworkErrorKind
import com.cagataysencan.otosistem.core.network.result.NetworkResponse
import com.cagataysencan.otosistem.data.mapper.PostMapper
import com.cagataysencan.otosistem.data.remote.source.PostRemoteDataSource
import com.cagataysencan.otosistem.di.DispatchersProvider
import com.cagataysencan.otosistem.domain.model.Post
import com.cagataysencan.otosistem.domain.repository.PostRepository
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
