package com.realityexpander.moviepresenter.model

import kotlinx.serialization.Serializable

@Serializable
data class TMDBResponse(
    val page: Int,
    val results: List<MovieModel>,
    val total_pages: Int,
    val total_results: Int
)