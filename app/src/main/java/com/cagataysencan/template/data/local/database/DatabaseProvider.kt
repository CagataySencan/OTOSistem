package com.cagataysencan.template.data.local.database

import android.content.Context
import androidx.room.Room
import com.cagataysencan.template.data.local.dao.PostDao
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Lazily builds [AppDatabase]. The database file is not created until [provide] or [providePostDao]
 * is called for the first time. Do not invoke from [com.cagataysencan.template.app.MainApplication].
 */
@Singleton
class DatabaseProvider @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME,
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    /** Returns the Room database instance; creates the on-disk database on first call. */
    fun provide(): AppDatabase = database

    /** Returns [PostDao] from the lazily initialized database. */
    fun providePostDao(): PostDao = provide().postDao()
}
