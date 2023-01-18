package com.realityexpander.moviepresenter.repository

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import com.realityexpander.moviepresenter.BuildConfig
import com.realityexpander.moviepresenter.Constants
import com.realityexpander.moviepresenter.model.HttpError
import com.realityexpander.moviepresenter.model.MovieModel
import com.realityexpander.moviepresenter.model.Post
import com.realityexpander.moviepresenter.model.PostEchoResponse.PostEchoResponse
import com.realityexpander.moviepresenter.model.TMDBResponse
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

val json = Json { ignoreUnknownKeys = true }

class MovieRepositoryImpl: MovieRepository {
//    override suspend fun fetchMovies1(): TMDBResponse? {
//        var response: TMDBResponse? = null
//        coroutineScope {
//            val endpoint: String = Constants.POPULAR_MOVIES_ENDPOINT + BuildConfig.TMDB_API_KEY
//            launch(Dispatchers.IO) {
//                val url = URL(endpoint)
//                with(url.openConnection() as HttpURLConnection) {
//                    requestMethod = "GET"
//
//                    inputStream.bufferedReader().use {
//                        it.lines().forEach { line ->
//                            response =
//                                Json.decodeFromString(TMDBResponse.serializer(), line)
//                        }
//                    }
//                }
//            }
//        }
//        return response
//    }

    override suspend fun fetchMovies(): TMDBResponse? {
        val endpoint: String = Constants.POPULAR_MOVIES_ENDPOINT + BuildConfig.TMDB_API_KEY

//        return coroutineScope {
//            withContext(Dispatchers.IO) {
//                async {
//                    val url = URL(endpoint) // Not allowed on main thread, must be on IO thread
//                    with(url.openConnection() as HttpURLConnection) {
//                        requestMethod = "GET"
//                        val data = BufferedInputStream(inputStream).readBytes().decodeToString()
//
//                        return@async Json.decodeFromString(TMDBResponse.serializer(), data)
//                    }
//                }.await()
//            }
//        }

        return coroutineScope {
            withContext(Dispatchers.IO) {
                val url = URL(endpoint) // Not allowed on main thread, must be on IO thread.
               try {
                    with(url.openConnection() as HttpURLConnection) {
                        requestMethod = "GET"
                        val data = BufferedInputStream(inputStream).readBytes().decodeToString()

                        Json.decodeFromString<TMDBResponse>(data)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                   null
                }
            }
        }
    }

    // Use: sendRequestString(requestBody = Json.encodeToString(post)); readResponse().map { Json.decodeFromString<PostEchoResponse>(it) }
    fun HttpURLConnection.sendRequestString(requestBody: String) {
        doOutput = true
        outputStream.use { os ->
            val input: ByteArray = requestBody.toByteArray()
            os.write(input, 0, input.size)
        }
    }

    inline fun <reified T> HttpURLConnection.sendRequest(requestBody: T): Result<String> {
        doOutput = true
        outputStream.use { os ->
            val input: ByteArray = json.encodeToString(requestBody).toByteArray()
            os.write(input, 0, input.size)
        }

        return readResponse()
    }

    fun HttpURLConnection.readResponse(): Result<String> {
        val code = responseCode  // forces the errorStream to be populated
        val errorResponse = errorStream?.let {
            BufferedInputStream(errorStream).readBytes().decodeToString()
        }

        if(code in 200..299) {
            val successResponse = BufferedInputStream(inputStream).readBytes().decodeToString()
            return Result.success(successResponse)
        }

        // Add the error code to the error response
        return Result.failure(Exception(Json.encodeToString(HttpError(code, errorResponse ?: ""))))
    }

//    @RequiresApi(Build.VERSION_CODES.O) // for Base64.getEncoder()
    suspend fun postMovie(post: Post): Result<PostEchoResponse> {
//        val endpoint: String = "https://postman-echo.com/post" // does a feedback echo of the post
        val endpoint: String = "https://97cd9eac-d7fb-49ad-bce2-24b923e89d55.mock.pstmn.io/post" // using PostMan mock server

        return withContext(Dispatchers.IO) {
            try {
                with(URL(endpoint).openConnection() as HttpURLConnection) {
                    requestMethod = "POST"
                    val user = "username"
                    val password = "password"
                    val authCredentials = "$user:$password"
//                    val encodedAuth = Base64.encodeToString(authCredentials.toByteArray(), Base64.NO_WRAP) // uses android.util.Base64
                    val encodedAuth = Base64.getEncoder().encodeToString(authCredentials.toByteArray(StandardCharsets.UTF_8))
                    setRequestProperty("Authorization", "Basic $encodedAuth")

//                    setRequestProperty("Content-type", "application/json; charset=UTF-8")
                    setRequestProperty("Content-Length", Json.encodeToString(post).toByteArray().size.toString())
                    setRequestProperty("x-api-key", "PMAK-63c75f54b3729d710e1cdaf6-39e48098a1e1e11a649ad6714f4245f78d")
                    doOutput = true

                    sendRequest(requestBody = post)
                        .map{// success case
                            json.decodeFromString<PostEchoResponse>(it)
                        }
                        .onSuccess { successResponse ->
                            Result.success(successResponse)
                        }
                        .onFailure { errorResponse ->
                            val error = json.decodeFromString<HttpError>(errorResponse.message ?: "")
                            println("Error code: ${error.code}, message: ${error.messageFromServer}")
                            Result.failure<PostEchoResponse>(errorResponse)
                        }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }

    override suspend fun fetchMoviePosters(movies: List<MovieModel>): List<MovieModel> {
        for (movie in movies) {
            coroutineScope {
                launch {
                    val posterUrl = movie.posterImgPath
                    val endpoint: String = Constants.MOVIE_POSTER_ENDPOINT +
                            Constants.MOVIE_POSTER_SMALL_SIZE +
                            posterUrl
                    val job = launch(Dispatchers.IO) {
                        val url = URL(endpoint)

                        with(url.openConnection() as HttpURLConnection) {
                            requestMethod = "GET"
                            val bufferedInputStream = BufferedInputStream(inputStream)

                            val img = BitmapFactory.decodeStream(bufferedInputStream)
                            movie.smallPosterImgBitmap = img
                        }
                    }
                    job.join()
                }
            }
        }

        return movies
    }

    override suspend fun fetchMoviePoster(movie: MovieModel): MovieModel {
        val posterUrl = movie.posterImgPath
        val endpoint: String = Constants.MOVIE_POSTER_ENDPOINT +
                Constants.MOVIE_POSTER_LARGE_SIZE +
                posterUrl

        coroutineScope {
            launch(Dispatchers.IO) {
                val url = URL(endpoint)
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"
                    val bufferedInputStream = BufferedInputStream(inputStream)
                    val img = BitmapFactory.decodeStream(bufferedInputStream)
                    movie.largePosterImgBitmap = img
                }
            }
        }

        return movie
    }
}