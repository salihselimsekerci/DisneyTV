package com.salihselimsekerci.disneyfilmkotlin.ui.main.view

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.salihselimsekerci.disneyfilmkotlin.R

class SplashFragment : Fragment() {
    //CountDownTimer için gerekli parametreleri burada tanımladım
    private val MAX_COUNT: Long = 3000
    private val STEP_COUNT: Long = 1000

    //Görünüm nesnesini oluşturdum
    private lateinit var v: View
    //Fragmentler arası geçiş için ilgili nesneyi oluşturdum
    private lateinit var navDirection: NavDirections

    //Görünümü geriye döndürdüm
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    //Görünüm olayı oluştuktan sonra gerekli işlemleri buraya yazdım
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view

        //CountDownTimer nesnesini burada oluşturup başlattım
        object : CountDownTimer(MAX_COUNT, STEP_COUNT) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                //Zaman dolunca moviefragment e geçiş yapcak otomatik
                navDirection = SplashFragmentDirections.actionSplashFragmentToMovieFragment()
                Navigation.findNavController(v).navigate(navDirection)
            }
        }.start()
    }
}