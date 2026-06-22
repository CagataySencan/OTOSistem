package com.cagataysencan.template.data.remote.source

import com.cagataysencan.template.core.network.result.NetworkResponse
import com.cagataysencan.template.core.network.result.safeApiCall
import com.cagataysencan.template.data.remote.api.ApiService
import com.cagataysencan.template.data.remote.dto.PostDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fetches post data from the remote API. Called by [PostRepositoryImpl], never from ViewModels.
 */
@Singleton
class PostRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {

    /** Loads all posts; wraps the result in [NetworkResponse]. */
    suspend fun getPosts(): NetworkResponse<List<PostDto>> {
        return safeApiCall { apiService.getPosts() }
    }

    /** Loads a single post by [id]; wraps the result in [NetworkResponse]. */
    suspend fun getPost(id: Int): NetworkResponse<PostDto> {
        return safeApiCall { apiService.getPost(id) }
    }
}
