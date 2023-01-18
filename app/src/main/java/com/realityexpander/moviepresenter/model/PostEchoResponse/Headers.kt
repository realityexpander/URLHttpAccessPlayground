package com.realityexpander.moviepresenter.model.PostEchoResponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Headers(
    @SerialName("accept-encoding")
    val acceptEncoding: String,
    @SerialName("content-length")
    val contentLength: String,
    @SerialName("content-type")
    val contentType: String,
    val host: String,
    @SerialName("user-agent")
    val userAgent: String,
    @SerialName("x-amzn-trace-id")
    val xAmznTraceId: String,
    @SerialName("x-forwarded-port")
    val xForwardedPort: String,
    @SerialName("x-forwarded-proto")
    val xForwardedProto: String
)