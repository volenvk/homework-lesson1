package com.example.homework_lesson1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.homework_lesson1.databinding.ActivityMainBinding
import com.example.homework_lesson1.databinding.ItemCinemaBinding
import com.example.homework_lesson1.model.Options
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var cinemaIndex by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        cinemaIndex = savedInstanceState?.getInt(KEY_CINEMA_INDEX) ?: -1
        val options = intent.getParcelableExtra<Options>(EXTRA_OPTIONS) ?: Options.DEFAULT

        createCinemaList(binding, options.count)
    }

    private fun createCinemaList(binding: ActivityMainBinding, count: Int){
        val mapIterator = image_resurces.iterator()
        val cinemaBindings = (0 until count).map { index ->
            val cinemaBinding = ItemCinemaBinding.inflate(layoutInflater)
            cinemaBinding.root.id = View.generateViewId()
            val next = mapIterator.next()
            cinemaBinding.cinemaImageView.setImageResource(next.second)
            cinemaBinding.cinemaTitleTextView.text = next.first
            cinemaBinding.root.tag = index
            cinemaBinding.root.setOnClickListener { onItemSelected(it) }
            binding.root.addView(cinemaBinding.root)
            cinemaBinding
        }
        binding.flowCinema.referencedIds = cinemaBindings.map { it.root.id }.toIntArray()
    }

    private fun onItemSelected(view: View){
        if (view.tag as? Int == cinemaIndex) {
            val intent = Intent(this, ViewCinema:: class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val KEY_CINEMA_INDEX = "key_cinema_index"
        const val EXTRA_OPTIONS = "extra_options"

        val image_resurces = sequenceOf(
            "Savage" to R.drawable.savage,
            "Avatar 2" to R.drawable.avatar_2,
            "Force Works" to R.drawable.force_works,
            "Eternal Warrior" to R.drawable.eternal_warrior,
            "Siege" to R.drawable.siege
        )
    }
}