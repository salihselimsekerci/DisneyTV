package com.salihselimsekerci.disneyfilmkotlin.ui.main.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.salihselimsekerci.disneyfilmkotlin.R
import com.salihselimsekerci.disneyfilmkotlin.databinding.FragmentDetailBinding
import com.salihselimsekerci.disneyfilmkotlin.ui.main.viewmodel.DetailViewModel
import com.salihselimsekerci.disneyfilmkotlin.util.Singleton

class DetailFragment : Fragment() {
    //Oluşturduğum viewmodeli burada değişkene atadım lateinit olarak (sonradan initialize)
    private lateinit var detailViewModel: DetailViewModel
    //Verileri yine databinding ile atacağım için bu sınıfın databinding özelliğini tanımladım
    private lateinit var dataBinding: FragmentDetailBinding
    //Seçilen filmin idsini almak için tanımladım
    private var uuid: Int = 0

    //Fragmentin görünümü ve ilgili databinding değişkenini burada initialize ettim
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    //Görünüm oluştuktan sonra gerekli ayarları burada yaptım
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Önceki fragmentten gelen değerleri argumen ile burada alıyorum
        arguments?.let {
            //uuid değerini burada aldım
            uuid = DetailFragmentArgs.fromBundle(it).movieUUID
            //ilgili viewmodel sınıfını burada initialize ettim
            detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
            //Seçilen filmin datasını burada çağırdım
            detailViewModel.getMovie(uuid)
            //Gözlemlemle olayını yani değişkenlerin durumuna göre olacak değişiklikleri burada fonksiyon ile çağırdım
            observeLiveData()
        }
    }

    //Gözlemle için oluşturduğum fonksiyon
    private fun observeLiveData(){
        detailViewModel.selectedMovie.observe(viewLifecycleOwner, Observer {
            it?.let {
                //İlgili datayı alıp görünümdeki parametreye atadım
                dataBinding.selectedMovie = it
            }
        })
    }

    //Burası ise anasayfada sadece exit dialog çalışsın geri kalan yerde çalışmasın diye yazıldı
    override fun onResume() {
        super.onResume()
        Singleton.mainScreenState = false
    }
}