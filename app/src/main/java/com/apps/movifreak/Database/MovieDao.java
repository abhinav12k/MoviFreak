package com.apps.movifreak.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Created by abhinav on 5/7/20.
 */
@Dao
public interface MovieDao {

    @Query("SELECT * FROM favMovie ORDER BY release_date")
    LiveData<List<FavMovie>> loadAllFavMoviesByReleaseDate();

    @Query("SELECT * FROM favMovie ORDER BY id")
    LiveData<List<FavMovie>> loadAllFavMoviesById();

    @Insert
    void insertMovie(FavMovie favMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(FavMovie favMovie);

    @Delete
    void deleteMovie(FavMovie favMovie);

    @Query("SELECT * FROM favMovie WHERE title LIKE :title || '%'")
    LiveData<List<FavMovie>> loadFavMovieByTitle(String title);

}
