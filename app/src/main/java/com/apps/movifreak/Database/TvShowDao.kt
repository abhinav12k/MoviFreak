package com.apps.movifreak.Database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by abhinav on 18/10/20.
 */

@Dao
interface TvShowDao {

    @Insert
    fun insertTvShow(tvShow: FavTvShow)

    @Delete
    fun deleteTvShow(tvShow: FavTvShow)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTvShow(tvShow: FavTvShow)

    @Query("SELECT * FROM favTvShows ORDER BY first_air_date")
    fun loadAllTvShowsByFirstAirDate(): LiveData<List<FavTvShow>>

    @Query("SELECT * FROM favTvShows ORDER BY id ASC")
    fun loadAllTvShowById(): LiveData<List<FavTvShow>>

    @Query("SELECT * FROM favTvShows WHERE title LIKE :title || '%'")
    fun loadTvShowByTitle(title: String): LiveData<List<FavTvShow>>

    @Query("SELECT * FROM favTvShows WHERE tvShowId = :TvShowId")
    fun loadTvShowByTvShowId(TvShowId: Long): FavTvShow

    @Query("SELECT * FROM favTvShows ORDER BY dateSaved")
    fun loadAllTvShowsByDateSaved(): LiveData<List<FavTvShow>>
}