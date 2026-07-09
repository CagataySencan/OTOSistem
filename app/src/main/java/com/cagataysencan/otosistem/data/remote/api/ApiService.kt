package com.cagataysencan.otosistem.data.remote.api

import com.cagataysencan.otosistem.data.remote.dto.PostDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API contract. Add new endpoints here and implement calls in remote data sources.
 */
interface ApiService {

    /** Fetches all posts from the remote API. */
    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    /** Fetches a single post by [id] from the remote API. */
    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): PostDto
}
