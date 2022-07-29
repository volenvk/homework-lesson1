package com.example.homework_lesson1.model

data class CommentDataRequest(
    val author: CommentAuthor,
    val save_state: SaveStateData
)
