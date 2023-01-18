package com.realityexpander.moviepresenter.model.PostEchoResponse

import kotlinx.serialization.Serializable

@Serializable
data class PostEchoResponse(
    val args: Args,
    val `data`: Data,
    val files: Files,
    val form: Form,
    val headers: Headers,
    val json: Json,
    val url: String
)