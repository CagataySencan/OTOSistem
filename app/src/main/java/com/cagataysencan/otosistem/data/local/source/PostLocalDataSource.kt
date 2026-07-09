package com.cagataysencan.otosistem.data.local.source

import com.cagataysencan.otosistem.data.local.database.DatabaseProvider
import com.cagataysencan.otosistem.data.mapper.PostEntityMapper
import com.cagataysencan.otosistem.di.DispatchersProvider
import com.cagataysencan.otosistem.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Reads and writes post cache via Room. Not wired to [PostRepositoryImpl] yet;
 * inject into repositories when offline/cache support is needed.
 */
@Singleton
class PostLocalDataSource @Inject constructor(
    private val databaseProvider: DatabaseProvider,
    private val postEntityMapper: PostEntityMapper,
    private val dispatchers: DispatchersProvider,
) {

    /** Observes cached posts as domain models; triggers database creation on first subscription. */
    fun observePosts(): Flow<List<Post>> {
        return databaseProvider.providePostDao()
            .observePosts()
            .map(postEntityMapper::toDomainList)
    }

    /** Reads all cached posts once on the IO dispatcher. */
    suspend fun getPosts(): List<Post> {
        return withContext(dispatchers.io) {
            postEntityMapper.toDomainList(
                databaseProvider.providePostDao().getPosts(),
            )
        }
    }

    /** Reads a single cached post by [id]; returns null when not cached. */
    suspend fun getPostById(id: Int): Post? {
        return withContext(dispatchers.io) {
            databaseProvider.providePostDao()
                .getPostById(id)
                ?.let(postEntityMapper::toDomain)
        }
    }

    /** Replaces the entire post cache with [posts]. */
    suspend fun savePosts(posts: List<Post>) {
        withContext(dispatchers.io) {
            databaseProvider.providePostDao().insertPosts(
                postEntityMapper.toEntityList(posts),
            )
        }
    }

    /** Clears all cached posts from the local database. */
    suspend fun clearPosts() {
        withContext(dispatchers.io) {
            databaseProvider.providePostDao().clearPosts()
        }
    }
}
