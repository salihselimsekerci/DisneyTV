package com.salihselimsekerci.disneyfilmkotlin.ui.main.view

import android.app.Activity
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.GridLayoutManager
import com.salihselimsekerci.disneyfilmkotlin.R
import com.salihselimsekerci.disneyfilmkotlin.ui.main.adapter.MoviesAdapter
import com.salihselimsekerci.disneyfilmkotlin.ui.main.adapter.decoration.GridManagerDecoration
import com.salihselimsekerci.disneyfilmkotlin.ui.main.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*
import android.graphics.Color
import android.widget.*
import com.salihselimsekerci.disneyfilmkotlin.util.Singleton


class MovieFragment : Fragment(), View.OnClickListener {
    //Görünüm nesnemei lateinit ettim ileride context kullanmam daha rahat olur
    private lateinit var v: View
    //Sıralama türü için alert dialog oluşturdum lateinit olarak
    private lateinit var mAlert: AlertDialog.Builder
    //Sıralama türü için arrayadapter oluşturdum
    private lateinit var mArrayAdapter: ArrayAdapter<CharSequence>
    //Seçilen sıralama türünü bu değişkene atadım
    private var selectedType: String? = null

    //Bu sınıf için oluşturduğum viewmodel sınıfını burada lateinit olarak oluşturdum
    private lateinit var movieViewModel: MovieViewModel
    //Bu kısım ile recyclerview için adapter oluşturdum ve içini boş bir liste olarak doldurdum
    private var mAdapter = MoviesAdapter(arrayListOf())

    //Menu ekleme işlemini burada yaptım
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //Fragment her eklendiğinde menüyü temizledim
        menu.clear()
        //Menüyü tekrar ekledim bağladım
        inflater.inflate(R.menu.movie_menu, menu)
        //Searchview için gerekli işlemleri burada yaptım
        //Iconların rengi vs dahil
        val searchItem: MenuItem = menu.findItem(R.id.movie_menu_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        val searchEditText =
            searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(resources.getColor(R.color.black))
        searchEditText.setHintTextColor(resources.getColor(R.color.black))
        val searchImgClose = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        searchImgClose.setImageResource(R.drawable.ic_bar_close)

        //İçine yazı yazıldıkça buradaki fonksiyon çalışacak
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Yazılan metin buradan alınıp adapter sınıfındaki filter nesnesine parametre olarak iletiliyor
                mAdapter.filter.filter(newText)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    //Oncreate kısmını ise fragment içinde menu oluşturmak için yazdım
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    //Görünüm verisini oluşturdum
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    //Görünüm işlemi oluştuktan sonraki işlemleri burada yapıyorum
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        //Gerekli ui işlemlerini bu fonksiyonu atadım ve çağırdım
        setupUI()

        //ilgili fragmentin viewmodel sınıfını burada initialize ettim
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        //Ardından datayı çağırdım
        movieViewModel.getDataFromAPI(v.context)

        //Swiperefresh dialog için refresh fonksiyonunu ve işlemleri burada yaptım
        movie_fragment_refreshLayout.setOnRefreshListener {
            //Refresh olunca recyclerview ve progressbarın durumunu deiştirdim
            movie_fragment_recyclerView.visibility = View.GONE
            movie_fragment_progressBar.visibility = View.VISIBLE
            //Sonradan tekrar verileri aldım
            movieViewModel.getDataFromAPI(v.context)
            //Ardından refersh işlemini bitirdim
            movie_fragment_refreshLayout.isRefreshing = false
        }

        //Gözlemleme işlemini burada fonksiyon olarak çağırdım
        observeLiveData()
    }

    //Gözlemleme işlemlerini burada yaptım
    private fun observeLiveData(){
        movieViewModel.movieList.observe(viewLifecycleOwner, Observer {
            it?.let {
                mAdapter.loadData(it)
                movie_fragment_recyclerView.visibility = View.VISIBLE
                movie_fragment_recyclerView.adapter = mAdapter
                movie_fragment_btnSort.setOnClickListener(this)
                movie_fragment_btnListType.setOnClickListener(this)
            }
        })

        movieViewModel.movieLoading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    movie_fragment_progressBar.visibility = View.VISIBLE
                    movie_fragment_recyclerView.visibility = View.GONE
                    movie_fragment_imgNoData.visibility = View.GONE
                } else
                    movie_fragment_progressBar.visibility = View.GONE
            }
        })

        movieViewModel.movieError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    movie_fragment_imgNoData.visibility = View.VISIBLE
                    movie_fragment_recyclerView.visibility = View.GONE
                    movie_fragment_progressBar.visibility = View.GONE
                }
            }
        })
    }

    //Ui ilemi için ilgili fonksiyonu burada oluşturdum
    private fun setupUI(){
        movie_fragment_recyclerView.setHasFixedSize(true)
        movie_fragment_recyclerView.layoutManager = GridLayoutManager(v.context, 2)
        movie_fragment_recyclerView.addItemDecoration(GridManagerDecoration(35, 25))
    }

    //Onclick olayları birden fazla olacağı için ilgili onclick olayını implement ederek kullandım
    override fun onClick(v: View?) {
        v?.let {
            when(it.id){
                R.id.movie_fragment_btnSort -> setSortedData()
                R.id.movie_fragment_btnListType -> showListDialog()
            }
        }
    }

    //Sort olaylarını bir fonksiyona bağlayıp çağırdım
    private fun setSortedData(){
        selectedType?.let {
            mAdapter.sortedData(it)
        }
    }

    //Listeleme türünü bu fonksiyon ile çağırdım ve gösterdim
    private fun showListDialog(){
        mAlert = AlertDialog.Builder(v.context, R.style.AlertDialogCustom)
        mAlert.setTitle(v.context.getString(R.string.movieAlertMessage))
        mAlert.setIcon(R.mipmap.ic_disney_logo)

        //Arrayadapter içini string dosyasındaki listten çağırarak doldurdum
        mArrayAdapter = ArrayAdapter.createFromResource(
            v.context,
            R.array.ListTypes,
            android.R.layout.select_dialog_singlechoice
        )
        mAlert.setNegativeButton(
            v.context.getString(R.string.movieAlertNegative)
        ) { dialog, which ->
            dialog.dismiss()
        }
        mAlert.setAdapter(
            mArrayAdapter
        ) { dialog, which ->
            //Seçilen listedeki ismi alıp bir değişkene atadım
            selectedType = mArrayAdapter.getItem(which) as String
            Toast.makeText(v.context, "$selectedType ${v.context.getString(R.string.movieAlertToastMsg)}", Toast.LENGTH_LONG).show()
        }

        mAlert.show()
    }

    //Burası ise uygulamadan çıkış dialogu için yazıldı
    override fun onResume() {
        super.onResume()
        Singleton.mainScreenState = true
    }
}