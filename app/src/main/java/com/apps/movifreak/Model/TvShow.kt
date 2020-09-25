package com.apps.movifreak.Model

import java.util.*

/**
 * Created by abhinav on 23/9/20.
 */

data class TvShow(
        val genre_ids: ArrayList<Int>,
        val title: String,
        val original_title: String,
        val popularity: Float,
        val vote_count: Long,
        val first_air_date: String,
        val backdrop_path: String,
        val original_language: String,
        val id: Long,
        val vote_average: Float,
        val overview: String,
        val poster_path: String,
        val landscapeImageUrl: String
)
