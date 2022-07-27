package com.example.homework_lesson1.model

data class CinemaSelectionInput(
    val image_id: Int,
    val cinema_info: String? = null,
    val saveData: CinemaSelectionOutput? = null
)
