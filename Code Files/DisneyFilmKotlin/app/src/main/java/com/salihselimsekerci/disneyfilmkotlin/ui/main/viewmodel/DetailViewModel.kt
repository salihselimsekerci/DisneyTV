package com.salihselimsekerci.disneyfilmkotlin.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.salihselimsekerci.disneyfilmkotlin.data.api.MovieDatabase
import com.salihselimsekerci.disneyfilmkotlin.data.model.Movie
import com.salihselimsekerci.disneyfilmkotlin.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {
    //MutableLivedata olarak movie oluşturdum bunu kullanarak veriyi alıcam
    val selectedMovie = MutableLiveData<Movie>()

    //Burada ilgili mutablelivedatasına movie değerini atadım
    fun getMovie(movieId: Int){
        launch {
            //ilgili sqlite dan veriyi id parametresi ile alıp çağırdım
            val movie = MovieDatabase(getApplication()).movieDAO().getMovie(movieId)
            //Ardından mutablelivedata ya atadım
            selectedMovie.value = movie
        }
    }
}