package com.example.homework_lesson1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedCinemaData(val is_like: Boolean?, val commentaries: List<CommentItem>?): Parcelable
