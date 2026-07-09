package com.cagataysencan.otosistem.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cagataysencan.otosistem.data.local.dao.PostDao
import com.cagataysencan.otosistem.data.local.entity.PostEntity

/**
 * Room database definition. Built lazily by [DatabaseProvider] on first local data access.
 */
@Database(
    entities = [PostEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    /** Returns the DAO for post cache operations. */
    abstract fun postDao(): PostDao

    companion object {
        /** On-disk database file name; used by [DatabaseProvider]. */
        const val DATABASE_NAME = "app_database"
    }
}
