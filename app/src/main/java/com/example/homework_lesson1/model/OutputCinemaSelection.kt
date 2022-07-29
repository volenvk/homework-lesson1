package com.example.homework_lesson1.model

data class OutputCinemaSelection(
    val isLike: Boolean,
    val comments: List<CommentData>,
    val resultCode: Int? = null
)
