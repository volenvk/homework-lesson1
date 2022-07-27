package com.example.homework_lesson1.model

data class CinemaSelectionOutput(
    val isLike: Boolean,
    val comment: String,
    val resultCode: Int? = null
)
