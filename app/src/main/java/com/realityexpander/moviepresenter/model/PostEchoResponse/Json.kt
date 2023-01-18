package com.realityexpander.moviepresenter.model.PostEchoResponse

import kotlinx.serialization.Serializable

@Serializable
data class Json(
    val body: String,
    val id: Int,
    val title: String
)