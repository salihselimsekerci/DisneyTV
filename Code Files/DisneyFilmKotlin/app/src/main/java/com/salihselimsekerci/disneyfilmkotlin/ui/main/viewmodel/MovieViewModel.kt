package com.salihselimsekerci.disneyfilmkotlin.ui.main.viewmodel

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.salihselimsekerci.disneyfilmkotlin.R
import com.salihselimsekerci.disneyfilmkotlin.data.api.MovieDatabase
import com.salihselimsekerci.disneyfilmkotlin.data.model.Movie
import com.salihselimsekerci.disneyfilmkotlin.data.repository.MovieRepository
import com.salihselimsekerci.disneyfilmkotlin.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : BaseViewModel(application) {
    //Progressbarı oluşturdum veri yüklenene kadar görünecek
    private lateinit var mProgressDialog: ProgressDialog
    //Film verilerini alacağım nesneyi oluşturdum
    private val movieRepository = MovieRepository()
    //Rxjavanın geridiği bellek kontrol işlemini bu nesne ile yapıyorum
    private val disposable = CompositeDisposable()

    //İlgili mutablelivedata işlemlerini burada tanımladım
    val movieList = MutableLiveData<List<Movie>>()
    val movieLoading = MutableLiveData<Boolean>()
    val movieError = MutableLiveData<Boolean>()

    //Bu fonksiyon ile internetten veri alıyoruz
    fun getDataFromAPI(context: Context){
        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage(context.getString(R.string.progressMsg))
        mProgressDialog.show()
        movieLoading.value = true

        //Verilerin belleği doldurmadan (şişirmeden) alma olayını burada yaptık
        //burası aynı zamanda veri alınırsa geriye onsuccess veya onfailure olarak dönüyor
        disposable.add(
            movieRepository.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Movie>>(){
                    override fun onSuccess(t: List<Movie>) {
                        saveDataSQLite(t)
                    }

                    override fun onError(e: Throwable) {
                        movieLoading.value = false
                        getDataFromSQLite(context)
                    }
                })
        )
    }

    //Bu fonksiyon sqlitedan verileri almak için yazdım
    private fun getDataFromSQLite(context: Context){
        movieLoading.value = true

        launch {
            val movieList = MovieDatabase(getApplication()).movieDAO().getAllMovies()

            //Sqlite datasında veri varsa alınacak
            if (movieList.isNotEmpty()){
                setMovies(movieList)
                Toast.makeText(getApplication(), context.getString(R.string.sqliteSuccess), Toast.LENGTH_SHORT).show()
            }else{ //Veri yoksa hata değerini true yapacak
                movieError.value = true
                Toast.makeText(getApplication(), context.getString(R.string.sqliteError), Toast.LENGTH_SHORT).show()
            }

            //İlgili progress dialogu bu fonksiyon ile kontrol edip kapatıyorum
            closeTheProgressDialog()
        }
    }

    //Bu fonksiyon ilgili verileri sqlita kayıt ediyor
    private fun saveDataSQLite(t: List<Movie>){
        launch {
            val movieDao = MovieDatabase(getApplication()).movieDAO()
            movieDao.deleteAllMovies()
            val longList = movieDao.insertAll(*t.toTypedArray())

            for (i in t.indices)
                t[i].uuid = longList[i].toInt()

            //Verileri kayıt edildikten ve uuidler ayarlandıktan sonra ilgili data fonksiyona gönderiliyor
            setMovies(t)
        }
    }

    //Datayı burada set ediyoruz
    private fun setMovies(t: List<Movie>){
        movieLoading.value = false
        movieList.value = t
        closeTheProgressDialog()
    }

    //Uygulama kökten kapanırsa oluşan disposable kontrolü temizlenecek böylecek sağlıklı çalışma yapmış olacaz
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    //Progressbar için yazdığım fonksiyon
    private fun closeTheProgressDialog(){
        if (mProgressDialog.isShowing)
            mProgressDialog.dismiss()
    }
}