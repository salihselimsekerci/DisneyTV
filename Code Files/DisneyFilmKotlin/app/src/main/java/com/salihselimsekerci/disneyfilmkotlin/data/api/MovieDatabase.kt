package com.salihselimsekerci.disneyfilmkotlin.data.api

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.salihselimsekerci.disneyfilmkotlin.data.model.Movie

/*Sqlite veritabanın + tablonun oluşması gibi işlemleri burada ayarladım
ve bu sınıfı kullanarak crud işlemlerini yani dao sınıfındaki işlemlere ulaşıcam*/
@Database(entities = arrayOf(Movie::class), version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDAO() : MovieDAO

    companion object{
        @Volatile private var instance: MovieDatabase? = null

        operator fun invoke(context: Context) = instance ?: synchronized(Any()){
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, MovieDatabase::class.java, "MovieDatabase"
        ).build()
    }
}