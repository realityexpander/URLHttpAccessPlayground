package com.realityexpander.moviepresenter.repository

import com.realityexpander.moviepresenter.model.MovieModel
import com.realityexpander.moviepresenter.model.TMDBResponse

interface MovieRepository {

    suspend fun fetchMovies(): TMDBResponse?

    suspend fun fetchMoviePosters(movies: List<MovieModel>): List<MovieModel>

    suspend fun fetchMoviePoster(movie: MovieModel): MovieModel

}