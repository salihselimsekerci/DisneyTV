package com.salihselimsekerci.disneyfilmkotlin.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.salihselimsekerci.disneyfilmkotlin.R

//Imageview nesnesinden yeni bir fonksiyon türettim
fun ImageView.downloadImageUrl(imageUrl: String?){
    //Resim yüklenirken üzerinde bir progressdailog çıkması ve yüklenmemesi durumunda gösterilecek resimi ayarladım
    val options = RequestOptions()
        .placeholder(placeHolderProgressBar(context))
        .error(R.mipmap.ic_launcher_round)

    //Gelen resim urlini glide ile yükledim
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(imageUrl)
        .into(this)
}

//Circulerprogressbar görünümü bu fonksiyon ile yapıyorum
fun placeHolderProgressBar(context: Context) : CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

//Bu fonksiyon görünüm tarafında çağrılacak ve ilgili imageview içine resim yüklenecek
//Çağırma şekli parametre çağırır gibi olacak
@BindingAdapter("android:downloadImg")
fun downloadImage(view: ImageView, url: String?){
    view.downloadImageUrl(url)
}