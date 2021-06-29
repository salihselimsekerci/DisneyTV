package com.salihselimsekerci.disneyfilmkotlin.data.repository

import com.salihselimsekerci.disneyfilmkotlin.data.api.MovieAPI
import com.salihselimsekerci.disneyfilmkotlin.data.model.Movie
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

//Retrofit kısmını burada oluşturdum
class MovieRepository {
    private val BASE_URL: String = "https://gist.githubusercontent.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MovieAPI::class.java)

    //Bu fonksiyon ile ise internetten verileri alıp geri dönderdim
    fun getData() : Single<List<Movie>> {
        return retrofit.getMovies()
    }
}