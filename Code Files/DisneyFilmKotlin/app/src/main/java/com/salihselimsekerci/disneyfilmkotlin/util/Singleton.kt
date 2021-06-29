package com.salihselimsekerci.disneyfilmkotlin.util

class Singleton {
    /*Sadece moviefragment (ana sayfa) kısmındayken çıkış dialogun çalışması ve geri
    kalan durumlar için ise fragmentlere dönülmesi için bu tarz bir yapı kullanıldı*/
    companion object{
        var mainScreenState: Boolean = false
    }
}