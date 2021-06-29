package com.salihselimsekerci.disneyfilmkotlin.data.api

import com.salihselimsekerci.disneyfilmkotlin.data.model.Movie
import io.reactivex.Single
import retrofit2.http.GET

/*Verileri internetten alacağım baseurl dışında paremetreyi aldım buraya
verileri ise rxjava ile aldım (Single) bu durum bizim verileri alırken
belleği şişirmeden kullanmamızı sağlayacak böylece uygulama daha hızlı çalışacak*/
interface MovieAPI {
    @GET("skydoves/aa3bbbf495b0fa91db8a9e89f34e4873/raw/a1a13d37027e8920412da5f00f6a89c5a3dbfb9a/DisneyPosters.json")
    fun getMovies() : Single<List<Movie>>
}