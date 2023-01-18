package com.realityexpander.moviepresenter.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(val title: String, val body: String, val id: Int)