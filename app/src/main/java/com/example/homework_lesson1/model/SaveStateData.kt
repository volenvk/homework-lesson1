package com.example.homework_lesson1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SaveStateData(
    var is_like: Boolean = false,
    val commentaries: MutableList<CommentItem> = mutableListOf()
): Parcelable {
    override fun toString(): String {
        return "is_like: $is_like, commentaries: ${commentaries.size}"
    }
}
