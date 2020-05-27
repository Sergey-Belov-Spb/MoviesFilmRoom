package com.example.moviesfilmroom.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.moviesfilmroom.R
import com.example.moviesfilmroom.data.entity.MovieItem
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity(), MovieListFragment.MovieListListener,MovieListFavoriteFragment.MovieListListener {
    companion object{
        const val TAG = "MainActiviry"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initButtonListener()
        openAllMoviesList()
    }

    fun initButtonListener() {
        findViewById<BottomNavigationView>(R.id.navigationBottom).setOnNavigationItemSelectedListener {menuItem ->
            when(menuItem.itemId){
                R.id.action_allMovies ->{
                    Log.d(TAG,"action_allMovies")
                    openAllMoviesList()
                }
                R.id.action_favoriteMovies ->{
                    Log.d(TAG,"action_favoriteMovies")
                    openFavoriteMoviesList()
                }
            }
            false
        }
    }

    private fun openAllMoviesList(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,MovieListFragment(), MovieListFragment.TAG  )
            //.addToBackStack("Main")
            .commit()
    }

    private fun openFavoriteMoviesList(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,MovieListFavoriteFragment(), MovieListFavoriteFragment.TAG  )
            //.addToBackStack("Main")
            .commit()
    }

    private fun openDetailedFragment(movieItem: MovieItem) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,MovieDetailedFragment.newInstance(movieItem.title,movieItem.gitUrl),MovieDetailedFragment.TAG)
            .addToBackStack("Detaled")
            .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is MovieListFragment)
        {
            fragment.listener = this
            Log.d(TAG,"onAttachFragment -> MovieListFragment")
        }
        if (fragment is MovieListFavoriteFragment)
        {
            fragment.listener = this
            Log.d(TAG,"onAttachFragment -> MovieListFavoriteFragment")
        }
    }

    override fun onMovieSelected(moviesItemDetailed: MovieItem) {
        Log.d(TAG,"Show Detailed")
        openDetailedFragment(moviesItemDetailed)
    }
}

/*class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}*/
