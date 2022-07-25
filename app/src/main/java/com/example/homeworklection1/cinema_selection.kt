package com.example.homeworklection1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.homeworklection1.databinding.ItemCinemaBinding

class cinema_selection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cinema_selection)

    }

    private fun createList(count: Int){
        val cinemaBindings = (0 until count).map { index ->
            val cinemaBinding = ItemCinemaBinding.inflate(layoutInflater)
            cinemaBinding.root.id = View.generateViewId()
            cinemaBinding.cinemaImageView.setImageResource(R.drawable.savage)
            cinemaBinding.cinemaTitleTextView.text = "savage"
            cinemaBinding.root.tag = index
            cinemaBinding
        }
    }
}