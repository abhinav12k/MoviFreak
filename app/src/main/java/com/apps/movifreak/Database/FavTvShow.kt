package com.apps.movifreak.Database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by abhinav on 18/10/20.
 */
@Entity(tableName = "favTvShows")
@Parcelize
data class FavTvShow(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        var title: String,
        var original_name: String,
        var popularity: Double,
        var vote_count: Long,
        var first_air_date: String,
        var backdrop_path: String,
        var tvShowId: Long,
        var vote_average: Float,
        var overview: String,
        var poster_path: String,
        var landscapeImageUrl: String,
        var dateSaved: Date
) : Parcelable