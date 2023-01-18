package com.realityexpander.moviepresenter.ui.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realityexpander.moviepresenter.Utils
import com.realityexpander.moviepresenter.model.MainViewModel

@Composable
fun MovieView(viewModel: MainViewModel) {

    val isLoading: Boolean = viewModel.inLoadingState.value

    viewModel.fetchMoviePoster()

    viewModel.movieItemPressed?.let { movie ->
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            CircularProgressBarIndicator(shouldBeDisplayed = isLoading)
            Column(modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(text = movie.originalTitle,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Row(horizontalArrangement = Arrangement.Center) {
                    movie.largePosterImgBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = movie.originalTitle
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.Center) {
                    Text(text = "Released : " + Utils.reverseDateFormat(movie.releaseDate), fontSize = 27.sp)
                }

                Row(horizontalArrangement = Arrangement.Center) {
                    Text(modifier = Modifier.padding(5.dp),
                        text = movie.movieOverview,
                        fontSize = 23.sp,
                        textAlign = TextAlign.Center)
                }

                Row(horizontalArrangement = Arrangement.Center) {
                    Icon(modifier = Modifier.size(35.dp),
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Star Icon",
                        tint = Color.Yellow)
                    Text(modifier = Modifier.padding(5.dp),
                        text = "Rating: " + movie.voteAvg.toString(),
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        color = Utils.getColorRating(movie.voteAvg))
                    Icon(modifier = Modifier.size(35.dp),
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Star Icon",
                        tint = Color.Yellow)
                }
            }
        }
    }


}