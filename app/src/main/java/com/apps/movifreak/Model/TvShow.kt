package com.apps.movifreak.Model

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by abhinav on 23/9/20.
 */

class TvShow (
        var genre_ids: ArrayList<Int>,
        var title: String,
        var original_name: String,
        var popularity: Double,
        var vote_count: Long,
        var first_air_date: String,
        var backdrop_path: String,
        var id: Long,
        var vote_average: Float,
        var overview: String,
        var poster_path: String,
        var landscapeImageUrl: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            arrayListOf<Int>().apply { parcel.readArrayList(Int::class.java.classLoader) },
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readDouble(),
            parcel.readLong(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readLong(),
            parcel.readFloat(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    constructor() : this(ArrayList<Int>(), "", "", 0.0, 0, "",
            "", 0, 0f, "", "", "")

    override fun toString(): String {
        return "TvShow{" +
                "title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", vote_average=" + vote_average +
                ", first_air_date='" + first_air_date + '\'' +
                ", id=" + id +
                ", poster_path='" + poster_path + '\'' +
                ", vote_count=" + vote_count +
                ", popularity=" + popularity +
                ", genre_ids=" + genre_ids +
                ", landscapeImageUrl='" + landscapeImageUrl + '\'' +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(genre_ids as List<*>?)
        parcel.writeString(title)
        parcel.writeString(original_name)
        parcel.writeDouble(popularity)
        parcel.writeLong(vote_count)
        parcel.writeString(first_air_date)
        parcel.writeString(backdrop_path)
        parcel.writeLong(id)
        parcel.writeFloat(vote_average)
        parcel.writeString(overview)
        parcel.writeString(poster_path)
        parcel.writeString(landscapeImageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TvShow> {
        override fun createFromParcel(parcel: Parcel): TvShow {
            return TvShow(parcel)
        }

        override fun newArray(size: Int): Array<TvShow?> {
            return arrayOfNulls(size)
        }
    }
}
