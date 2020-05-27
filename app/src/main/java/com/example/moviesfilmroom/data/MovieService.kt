package com.example.moviesfilmroom.data

import com.example.moviesfilmroom.data.entity.MovieItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface MovieService {
    @GET("films")
    fun getUserRepos(): Call<List<MovieItem>>

    //@GET("users/{user}/repos")
    //fun getUserRepos(@Path("user") user: String): Call<List<MovieItem>>
}