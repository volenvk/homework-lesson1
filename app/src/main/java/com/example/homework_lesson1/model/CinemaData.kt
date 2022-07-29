package com.example.homework_lesson1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CinemaData(val image_id: Int?, val cinema_info: String?): Parcelable