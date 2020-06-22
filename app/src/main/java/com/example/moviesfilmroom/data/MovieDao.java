package com.example.moviesfilmroom.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.moviesfilmroom.data.entity.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * from movie_table ORDER BY movie ")
    LiveData<List<Movie>> getMovies();

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert (Movie movie);

    @Query("DELETE FROM movie_table")
    void deleteAll();

    class MovieRoomDatabase {
    }
}
