package com.salihselimsekerci.disneyfilmkotlin.ui.main.adapter.listener

import android.view.View

/*Bu sınıf ise recyclerviewdeki itemlere tıklanması için yazıldı
bunu yapabilmek için databinding özelliğinden yararlandım
yani ilgili görünüm(linearlayout) nesnesinin onclick özelliğine
bunu atarak kullandım*/
interface MovieOnClickListener {
    fun onClickListener(v: View)
}