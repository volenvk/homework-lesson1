package com.example.homework_lesson1.model

data class OutputCinemaSelection(
    val is_like: Boolean,
    val commentaries: List<CommentItem>,
    val result_code: Int? = null
)
