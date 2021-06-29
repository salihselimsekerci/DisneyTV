package com.salihselimsekerci.disneyfilmkotlin.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/*Bu sınıfım hem veritabanı hem internetten veri almak için yazdım
ColumnInfo kısımları ile tablodaki kolon isimleri verdim SerializedName
ile ise jsondan gelen verilerin ismini alıp ilgili değişkenlere atadım*/
@Entity
data class Movie(
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val movieId: Int?,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val movieName: String?,

    @ColumnInfo(name = "release")
    @SerializedName("release")
    val movieRelease: String?,

    @ColumnInfo(name = "playtime")
    @SerializedName("playtime")
    val moviePlayTime: String?,

    @ColumnInfo(name = "description")
    @SerializedName("description")
    val movieDescription: String?,

    @ColumnInfo(name = "plot")
    @SerializedName("plot")
    val moviePilot: String?,

    @ColumnInfo(name = "poster")
    @SerializedName("poster")
    val moviePoster: String?
) {
    //Bu kısımda ise otomatik id atıyor
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}