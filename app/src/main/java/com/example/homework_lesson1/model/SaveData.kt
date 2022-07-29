package com.example.homework_lesson1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SaveData(
    var index: Int = -1,
    val states: MutableMap<Int, SaveStateData> = mutableMapOf()
): Parcelable
