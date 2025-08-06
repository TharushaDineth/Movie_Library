package com.example.movie_library.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Define the database configuration and list of entities
@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    // DAO to access movie data
    abstract fun movieDao(): MovieDao

    // Singleton pattern to get the database instance
    companion object {

        // Volatile instance to ensure thread-safe access
        @Volatile private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            // Create the database if it doesn't exist yet
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}