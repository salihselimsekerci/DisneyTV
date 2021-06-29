package com.salihselimsekerci.disneyfilmkotlin.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.salihselimsekerci.disneyfilmkotlin.R
import com.salihselimsekerci.disneyfilmkotlin.data.model.Movie
import com.salihselimsekerci.disneyfilmkotlin.databinding.MovieItemBinding
import com.salihselimsekerci.disneyfilmkotlin.ui.main.adapter.listener.MovieOnClickListener
import com.salihselimsekerci.disneyfilmkotlin.ui.main.view.MovieFragmentDirections
import kotlinx.android.synthetic.main.movie_item.view.*
import java.util.*
import kotlin.collections.ArrayList

//Bu sınıfım recyclerview için yazılmış bir adapter sınıfı
class MoviesAdapter(val movieList: ArrayList<Movie>) : RecyclerView.Adapter<MoviesAdapter.MoviesHolder>(), MovieOnClickListener, Filterable {
    /*Verileri görünümden almak için bu değişkeni tanımladım
    ayrıca bu değişken ile viewholder nesnesini geri dönderdim*/
    private lateinit var v: MovieItemBinding
    //Görünümü entegre etmek için tanımladım
    private lateinit var inflater: LayoutInflater
    //Fragmentler arasındaki geçiş için tanımladım
    private lateinit var navDirection: NavDirections
    //Searchview için bu değişkeni tanımladım
    private lateinit var movieListFull: ArrayList<Movie>
    //Sıralama vs işlemleri için ise bu değişkeni tanımladım
    private lateinit var typeList: List<String>
    //Verinin olup olmadığı durum için tanımladım
    private var dataState: Boolean = false

    //Bu fonksiyon ile görünümü oluşturdum ve MoviesHolder olarak geri dönderdim
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        inflater = LayoutInflater.from(parent.context)
        v = DataBindingUtil.inflate<MovieItemBinding>(inflater, R.layout.movie_item, parent, false)
        return MoviesHolder(v)
    }

    /*Görünümdeki nesnelerin içini doldurmayı burada yaptım
    bu şekilde doldurma işlemini ise databinding sayesinde yaptım*/
    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        holder.view.movie = movieList.get(position)
        holder.view.listener = this
    }

    //Bu fonksiyon ile gelen veri sayısını alıp geri dönderdim
    override fun getItemCount(): Int {
        return movieList.size
    }

    //Görünüm için yazdığım sınıf
    class MoviesHolder(var view: MovieItemBinding) : RecyclerView.ViewHolder(view.root)

    //Verileri bu fonksiyon ile alıp doldurdum
    fun loadData(movies: List<Movie>){
        movieList.clear()
        movieList.addAll(movies)
        movieListFull = ArrayList(movieList)
        if (movieList.size > 0)
            dataState = true
        notifyDataSetChanged()
    }

    //Sıralama işlemlerini bu fonksiyon ile yaptım
    fun sortedData(type: String){
        movieList.clear()
        typeList = v.root.context.resources.getStringArray(R.array.ListTypes).toList()

        if (type.equals(typeList.get(0)))
            movieList.addAll(movieListFull.sortedBy { it.movieName })
        else if (type.equals(typeList.get(1)))
            movieList.addAll(movieListFull.sortedByDescending { it.movieName })
        else if (type.equals(typeList.get(2)))
            movieList.addAll(movieListFull.sortedBy { it.uuid })
        else
            movieList.addAll(movieListFull.sortedByDescending { it.uuid })

        movieListFull.clear()
        movieListFull = ArrayList(movieList)
        notifyDataSetChanged()
    }

    //Bu fonksiyon ise oluşturduğum interfaceden geliyor tıklama işlemleri için
    override fun onClickListener(v: View) {
        val uuid: Int = v.movie_item_txtUUID.text.toString().toInt()
        navDirection = MovieFragmentDirections.actionMovieFragmentToDetailFragment(uuid)
        Navigation.findNavController(v).navigate(navDirection)
    }

    /*Filtreleme işlemini implement ettiğim için bu fonksiyonu buraya ekledim
    bu fonksiyon ile filtreleme olayını yapabilecem ancak geriye Filter döndürmeli*/
    override fun getFilter(): Filter {
        return movieFilter
    }

    //Filter nesnesini moviefilter olarak türettim
    private var movieFilter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            //Arama işlemi yaparken verileri buna dolduruyorum
            val filteredList : ArrayList<Movie> = ArrayList()

            //Gelen veriyi burada kontrol edip, bulunması durumunda içine dolduruyorum
            constraint?.let {
                if (it.isNotEmpty()){
                    val filterPattern: String = it.toString()

                    for (movie in movieListFull){
                        if (movie.movieName!!.contains(filterPattern))
                            filteredList.add(movie)
                    }
                }else
                    filteredList.addAll(movieListFull)
            }

            //Filtreleme sonucunu bu değişkene atadım
            val results: FilterResults = FilterResults()
            //Bulunan veri listesini bu results değişkenine atadım values parametre sayesinde
            results.values = filteredList

            //results değişkenini geriye döndürdüm
            return results
        }

        //Döndürülen results datası buraya düşüyor ve verilerin yeniden doldurulmasını burada yaptım tekrar
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.let {
                if (dataState){
                    movieList.clear()
                    movieList.addAll(it.values as ArrayList<Movie>)
                    notifyDataSetChanged()
                }
            }
        }
    }
}