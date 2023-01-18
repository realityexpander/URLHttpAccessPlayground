package com.realityexpander.moviepresenter.model

import kotlinx.serialization.Serializable

@Serializable
    data class HttpError(val code: Int, val messageFromServer: String) : Exception()