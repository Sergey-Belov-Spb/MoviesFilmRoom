package com.example.moviesfilmroom.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.moviesfilmroom.R
import com.example.moviesfilmroom.data.entity.MovieItem
import com.example.moviesfilmroom.presentation.viewmodel.MovieListViewModel
import java.util.ArrayList
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar



class MovieListFragment : Fragment() {
    companion object{
        const val TAG = "MovieListFragment"
    }

    var listener :MovieListListener? = null
    private var viewModel: MovieListViewModel? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: MovieAdapter? = null
    private var progressBar: ProgressBar? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is MovieListListener){
            listener = activity as MovieListListener
        } else {
            throw Exception("Activity must implement MovieListListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()

        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        viewModel = ViewModelProviders.of(activity!!).get(MovieListViewModel::class.java!!)
        viewModel!!.moviesAll.observe(this.viewLifecycleOwner, Observer<List<MovieItem>> { repos ->

            if (repos.size==0) {viewModel!!.onGetDataClick()}
            else
            {adapter!!.setItems(repos,view)}
        })
        viewModel!!.error.observe(this.viewLifecycleOwner, Observer<String> { error ->

            progressBar?.visibility = View.INVISIBLE
            val mySnackbar =  this.view?.let { Snackbar.make(it, "Ошибка соединения с интернетом.", Snackbar.LENGTH_LONG) }
            mySnackbar?.setAction(
                "Повторить запрос?",
                {
                    viewModel!!.onGetDataClick()
                    progressBar?.visibility = View.VISIBLE
                    Log.d(TAG,"NEW LOAD!!!! from mySnackbar ")
                })
            mySnackbar?.show()
        })

        /* <!--
        view.findViewById<View>(R.id.getDataBtn).setOnClickListener { v ->
            viewModel!!.onGetDataClick()
            progressBar?.visibility = View.VISIBLE
        }*/

        viewModel!!.onGetDataClick()
        progressBar?.visibility = View.VISIBLE
    }

    private fun initRecycler() {
        adapter = MovieAdapter(LayoutInflater.from(context), object : MovieAdapter.OnRepoSelectedListener {
            override fun onRepoSelect(item: MovieItem, addToFavorite: Boolean) {
                if (addToFavorite==false) { listener?.onMovieSelected(item)}
                else {
                    viewModel!!.onMovieSelect(item,addToFavorite)
                    adapter?.notifyDataSetChanged()
                }
            }
        })
        recyclerView = view!!.findViewById(R.id.recyclerView)
        recyclerView!!.adapter = adapter

        recyclerView?.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                Log.d(TAG, "dx:$dx dy:$dy ")
                val layoutManager= recyclerView?.layoutManager as LinearLayoutManager
                val x = layoutManager.findLastVisibleItemPosition()
                val sizeItems = adapter?.itemCount
                Log.d(TAG,"layoutManager.findLastVisibleItemPosition() = $x sizeItems = $sizeItems")
                if ((layoutManager.findLastVisibleItemPosition()+1)== adapter?.itemCount){

                    //listener?.onMoviesSelected(MoviesItem(1,"1","1",false),-1,0)
                    Log.d(TAG,"NEW LOAD!!!! ")
                    viewModel!!.onGetDataClick()
                    progressBar?.visibility = View.VISIBLE
                }
            }
        })
    }
    /* class RecyclerView.ViewHolder */
    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameMovie=itemView.findViewById<TextView>(R.id.nameMovieInAll)
        val picFavorite=itemView.findViewById<ImageView>(R.id.imageFavoriteAll)
        val imageFilm = itemView.findViewById<ImageView>(R.id.imageMovieInAll)

        fun bind(item: MovieItem) {
            nameMovie.text = item.title
            if (item.favorite== true) {picFavorite.setImageResource(R.drawable.ic_favorite_black_24dp)}
            else {picFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)}

            Glide.with(imageFilm.context)
                .load(item.gitUrl)
                .placeholder(R.drawable.ic_image_blue)
                .error(R.drawable.ic_error_blue)
                .override(imageFilm.resources.getDimensionPixelSize(R.dimen.image_size))
                .centerCrop()
                .into(imageFilm)
            //(itemView as TextView).text = url
        }
    }
    /* class RecyclerView.Adapter */
    class MovieAdapter(private val inflater: LayoutInflater, private val listener: OnRepoSelectedListener) : RecyclerView.Adapter<MovieViewHolder>() {
        private val items = ArrayList<MovieItem>()

        fun setItems(repos: List<MovieItem>,view: View) {
            //items.clear()
            view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE
            items.addAll(repos)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            return MovieViewHolder(inflater.inflate(R.layout.item_movie, parent, false))
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            holder.bind(items[position])
            holder.itemView.findViewById<ImageView>(R.id.imageFavoriteAll).setOnClickListener{
                    v -> listener.onRepoSelect(items[position],true)

            }
            holder.itemView.findViewById<ImageView>(R.id.imageMovieInAll).setOnClickListener{
                //holder.itemView.setOnClickListener {
                    v -> listener.onRepoSelect(items[position],false)
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        interface OnRepoSelectedListener {
            fun onRepoSelect(item: MovieItem, addToFavorite: Boolean)
        }
    }

    interface MovieListListener {
        fun onMovieSelected(moviesItemDetailed: MovieItem)
    }
}