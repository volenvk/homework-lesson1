package com.example.homework_lesson1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class CinemaSelectionRequest(
    val cinema_info: CinemaInfoData? = null,
    val save_state: SaveStateData? = null
)

@Parcelize
data class CinemaInfoData(val image_id: Int? = null, val description: String? = null): Parcelable
