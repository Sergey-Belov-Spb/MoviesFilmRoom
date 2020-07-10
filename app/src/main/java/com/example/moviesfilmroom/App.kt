package com.example.moviesfilmroom

import android.app.Application
import com.example.moviesfilmroom.data.MovieRepository
import com.example.moviesfilmroom.data.MovieService
import com.example.moviesfilmroom.domain.MovieInteractor
import com.example.moviesfilmroom.presentation.viewmodel.MovieListViewModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.moviesfilmroom.data.MovieRepositoryBase as MovieRepositoryBase

class App : Application(){

    lateinit var movieService: MovieService
    // lateinit var githubReposUpdater: GithubReposUpdater
    lateinit var movieInteractor: MovieInteractor

    var reposRepository = MovieRepository()

    override fun onCreate() {
        super.onCreate()

        instance = this

        initRetrofit()
        initInteractor()
        initBase()
    }

    private fun initInteractor() {
        movieInteractor = MovieInteractor(movieService, reposRepository)
    }
    private fun initRetrofit() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        movieService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(MovieService::class.java!!)

        ///githubReposUpdater = GithubReposUpdater(githubService)
    }
    private fun initBase(){
        //private val mRepositoryBase = MovieRepositoryBase(this)
        mRepositoryBase = MovieRepositoryBase(this)
        //mRepositoryBase?.MovieRepositoryBase(this)
    }


    companion object{
        const val BASE_URL = "https://my-json-server.typicode.com/shustreek/demo/"
        //const val BASE_URL = "https://api.github.com/"

        var mRepositoryBase : MovieRepositoryBase? = null
        var instance : App? = null
            private set
    }
}