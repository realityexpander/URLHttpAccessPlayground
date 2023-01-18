package com.realityexpander.moviepresenter.model.PostEchoResponse

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val body: String,
    val id: Int,
    val title: String
)