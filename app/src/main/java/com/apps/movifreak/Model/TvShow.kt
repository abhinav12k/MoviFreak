package com.apps.movifreak.Model

import java.util.*

/**
 * Created by abhinav on 23/9/20.
 */

class TvShow(
        var genre_ids: ArrayList<Int>,
        var title: String,
        var original_name: String,
        var popularity: Double,
        var vote_count: Long,
        var first_air_date: String,
        var backdrop_path: String,
        var original_language: String,
        var id: Long,
        var vote_average: Float,
        var overview: String,
        var poster_path: String,
        var landscapeImageUrl: String
) {
    constructor() : this(ArrayList<Int>(),"","",0.0,0,"","",
    "",0,0f,"","","")

}
