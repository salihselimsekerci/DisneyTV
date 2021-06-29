package com.salihselimsekerci.disneyfilmkotlin.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/*Birden fazla viewmodel sınıfımın olması durumu için hepsine coroutine
tanımlamak ve işlem yapmak yerine bunları tek bir çatı altına alıp ve
ilgili sınıflara implement ederek kullanmak üzere hazırladım*/
abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}