package com.example.homework_lesson1.model

data class CommentData(
    val author: CommentAuthor,
    val comment: String
)

data class CommentAuthor(val name: String, val color: Int)
