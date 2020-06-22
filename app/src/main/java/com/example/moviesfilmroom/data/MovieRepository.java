package com.example.moviesfilmroom.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Database;

import com.example.moviesfilmroom.data.entity.Movie;

import java.util.List;

class MovieRepositoryBase {

     private MovieDao mMovieDao;
     private LiveData<List<Movie>> mAllMovies;

     MovieRepositoryBase(Application application){

         MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
         mMovieDao = db.movieDao();
         mAllMovies = mMovieDao.getMovies();
     }

     LiveData<List<Movie>> getmAllMovie () {return mAllMovies; }


     void insert (Movie movie) {
         MovieRoomDatabase.databaseWriteExecutor.execute(() -> {
            mMovieDao.insert(movie);
         });
     }

}
