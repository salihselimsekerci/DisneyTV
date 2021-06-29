package com.salihselimsekerci.disneyfilmkotlin.ui.main.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

//Bu sınıf recyclerview içindeki itemler için aralarına boşluk vs koymak için yazıldı
//vSize = Vertical Size, hSize = Horizontal Size
class GridManagerDecoration(val vSize: Int, val hSize: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(hSize, vSize, hSize, vSize)
    }
}