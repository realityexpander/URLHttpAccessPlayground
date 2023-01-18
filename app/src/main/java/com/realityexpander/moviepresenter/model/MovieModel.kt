package com.realityexpander.moviepresenter.model

import android.graphics.Bitmap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class MovieModel(
    @SerialName("adult")
    val isAdultMovie: Boolean,
    @SerialName("backdrop_path")
    val backdropImgPath: String?,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    @SerialName("id")
    val movieId: Int,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("overview")
    val movieOverview: String,
    val popularity: Double,
    @SerialName("poster_path")
    val posterImgPath: String,
    @SerialName("release_date")
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    @SerialName("vote_average")
    val voteAvg: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    @Transient var smallPosterImgBitmap: Bitmap? = null,
    @Transient var largePosterImgBitmap: Bitmap? = null
)