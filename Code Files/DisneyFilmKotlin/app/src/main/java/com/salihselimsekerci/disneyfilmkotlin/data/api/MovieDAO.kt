package com.salihselimsekerci.disneyfilmkotlin.data.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.salihselimsekerci.disneyfilmkotlin.data.model.Movie

/*Verileri sqliteda tutmak için room yapısını kullandım ve onla ilgili
gerekli işlemleri buraya yazıp belirttim*/
@Dao
interface MovieDAO {
    @Insert
    suspend fun insertAll(vararg movies: Movie) : List<Long>

    @Query("SELECT * FROM movie")
    suspend fun getAllMovies() : List<Movie>

    @Query("SELECT * FROM movie WHERE uuid = :movieId")
    suspend fun getMovie(movieId: Int) : Movie

    @Query("DELETE FROM movie")
    suspend fun deleteAllMovies()
}