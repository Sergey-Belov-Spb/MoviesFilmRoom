package com.example.moviesfilmroom.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Movie {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movie")
    private String mMovie;

    public Movie(@NonNull String movie) { this.mMovie = movie; }

    @NonNull
    public String getMovie() { return this.mMovie; }
}