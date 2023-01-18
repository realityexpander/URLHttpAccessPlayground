package com.realityexpander.moviepresenter.model

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.realityexpander.moviepresenter.network.NetworkConnectivityManager
import com.realityexpander.moviepresenter.repository.MovieRepositoryImpl
import kotlinx.coroutines.launch
import java.net.Authenticator
import java.net.PasswordAuthentication


class MainViewModel(application: Application): AndroidViewModel(application) {

    private val movieRepository: MovieRepositoryImpl = MovieRepositoryImpl()
    private val networkConnectivityManager: NetworkConnectivityManager = NetworkConnectivityManager()
    val moviesList: MutableState<List<MovieModel>> = mutableStateOf(listOf())
    var inLoadingState: MutableState<Boolean> = mutableStateOf(true)
    var isInternetConnectionAvailable: MutableState<Boolean> = mutableStateOf(true)

    var movieItemPressed: MovieModel? = null

    init {

        if (!networkConnectivityManager.isNetworkConnected(application.applicationContext)) {
            inLoadingState.value = false
            isInternetConnectionAvailable.value = false
        } else {
            viewModelScope.launch {
                movieRepository.fetchMovies()?.let { response ->
                    var movies: List<MovieModel> = response.results
                    movies = movieRepository.fetchMoviePosters(movies)
                    moviesList.value = movies
                    inLoadingState.value = false
                }
            }
        }

        viewModelScope.launch {
            println("response is ${movieRepository.postMovie(Post("foo", "bar", 1))}")
        }
    }

    fun fetchMoviePoster() {
        movieItemPressed?.let { movie ->
            viewModelScope.launch {
                movieItemPressed = movieRepository.fetchMoviePoster(movie)
                inLoadingState.value = false
            }
        }
    }
}