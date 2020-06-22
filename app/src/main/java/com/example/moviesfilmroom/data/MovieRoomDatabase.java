package com.example.moviesfilmroom.data;


import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;

import com.example.moviesfilmroom.data.entity.Movie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Movie.class},version = 1,exportSchema = false)
abstract class MovieRoomDatabase extends RoomDatabase {
    abstract MovieDao movieDao();

    private static volatile MovieRoomDatabase INSTANCE;
    private static final int NUMBER_OF_TREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool (NUMBER_OF_TREADS);

    static MovieRoomDatabase getDatabase (final Context context){
        if (INSTANCE == null) {
            synchronized (MovieRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDatabase.class, "movie_database")
                            .addCallback(sMovieDatabaseCallback)
                            .build();
                }
            }

        }
        return INSTANCE;
    }

    private static final Callback sMovieDatabaseCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                MovieDao dao = INSTANCE.movieDao();
                dao.deleteAll();

                Movie movie = new Movie("Hello");
                dao.insert(movie);
                movie = new Movie("World");
                dao.insert(movie);

            });
        }
    };
}
