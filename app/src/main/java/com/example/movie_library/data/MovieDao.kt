package com.example.movie_library.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Data Access Object for Movie operations
@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    // Get all movies from the database.
    @Query("SELECT * FROM movies")
    suspend fun getAll(): List<Movie>
}