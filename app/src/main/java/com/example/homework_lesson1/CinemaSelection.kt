package com.example.homework_lesson1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.homework_lesson1.databinding.ActivityCinemaSelectionBinding
import com.example.homework_lesson1.databinding.ActivityMainBinding
import com.example.homework_lesson1.databinding.ItemCinemaBinding
import com.example.homework_lesson1.model.Options
import kotlin.properties.Delegates

class CinemaSelection : AppCompatActivity() {

    private lateinit var binding: ActivityCinemaSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaSelectionBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val desc = intent.getStringExtra(MainActivity.EXTRA_CINEMA_INFO) ?: throw IllegalArgumentException("Can't start without cinema description")
        val id = intent.getIntExtra(MainActivity.EXTRA_IMAGE_ID, -1)
        id.takeIf { it > 0 }?.let { binding.cinemaImageView.setImageResource(it) }
        binding.cinemaInfoTextView.text = desc
    }
}