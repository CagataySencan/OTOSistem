package com.cagataysencan.otosistem.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cagataysencan.otosistem.data.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for post cache operations. Access via [com.cagataysencan.otosistem.data.local.database.DatabaseProvider].
 */
@Dao
interface PostDao {

    /** Observes all cached posts; emits on every table change. */
    @Query("SELECT * FROM posts ORDER BY id ASC")
    fun observePosts(): Flow<List<PostEntity>>

    /** Reads all cached posts once from the local database. */
    @Query("SELECT * FROM posts ORDER BY id ASC")
    suspend fun getPosts(): List<PostEntity>

    /** Reads a single cached post by [id]; returns null if not found. */
    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: Int): PostEntity?

    /** Inserts or replaces posts in the cache; returns row ids for inserted items. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>): List<Long>

    /** Removes all cached posts from the local database; returns the number of rows deleted. */
    @Query("DELETE FROM posts")
    suspend fun clearPosts(): Int
}
