package com.example.moviesfilmroom.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesfilmroom.App
import com.example.moviesfilmroom.data.entity.MovieItem
import com.example.moviesfilmroom.domain.MovieInteractor
import java.util.ArrayList

class MovieListViewModel: ViewModel() {
    private val allMoviesLiveData = MutableLiveData<List<MovieItem>>()
    private val favoriteLiveData = MutableLiveData<List<MovieItem>>()
    private val errorLiveData = MutableLiveData<String>()
    private var activeInternetRequestData = false


    private val itemsFavorite = ArrayList<MovieItem>()

    private val githubInteractor = App.instance!!.movieInteractor

    val moviesAll: LiveData<List<MovieItem>>
        get() = allMoviesLiveData

    val moviesFavorite: LiveData<List<MovieItem>>
        get() = favoriteLiveData

    val error: LiveData<String>
        get() = errorLiveData


    fun onGetDataClick() {
        if (activeInternetRequestData == false) {
            activeInternetRequestData=true
            githubInteractor.getNewMovies(object : MovieInteractor.GetMoviesCallback {
                override fun onSuccess(newMovies: List<MovieItem>) {
                    activeInternetRequestData=false
                    allMoviesLiveData.postValue(newMovies)
                }

                override fun onError(error: String) {
                    activeInternetRequestData=false
                    errorLiveData.postValue(error)
                }
            })
        } else {
            Log.d("MovieListViewModel","Block New data Request!!")
        }
    }

    fun onMovieSelect(item: MovieItem, addToFavotite: Boolean) {
        if (addToFavotite){
            Log.d("MovieListViewModel","AddToFavorite")
            if (item.favorite == true) {
                item.favorite = false
                /*listOf(favoriteLiveData)
                favoriteLiveData.value*/
            }
            else {item.favorite = true }

            if ((item.favorite == true)&&(itemsFavorite.indexOf(item)==-1)) { itemsFavorite.add(item) }
            else {
                itemsFavorite.remove(item)
            }
            favoriteLiveData.postValue(allMoviesLiveData.value)
        }
    }
}