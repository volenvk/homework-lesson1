package com.example.homework_lesson1.model

data class InputCinemaSelection(
    val image_id: Int,
    val cinema_info: String? = null,
    val save_state: OutputCinemaSelection? = null
)
