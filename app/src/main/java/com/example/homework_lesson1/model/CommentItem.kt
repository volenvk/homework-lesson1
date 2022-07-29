package com.example.homework_lesson1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class CommentItem(val author: CommentAuthor, val comment: String, val date: Date = Date()) : Parcelable

@Parcelize
data class CommentAuthor(val name: String, val color: Int) : Parcelable
