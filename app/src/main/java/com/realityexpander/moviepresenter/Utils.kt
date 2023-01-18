package com.realityexpander.moviepresenter

import androidx.compose.ui.graphics.Color

class Utils {

    companion object {
        fun reverseDateFormat(date: String):String {
            val splitDate: List<String> = date.split("-")
            return splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0]
        }

        fun getColorRating(movieRating: Double): Color {
            return when(movieRating) {
                 in 8.0..10.0 -> Color(40, 180, 99)
                 in 6.5..7.9 -> Color(212, 172, 13)
                 else -> Color(203, 67, 53)
            }
        }
    }


}