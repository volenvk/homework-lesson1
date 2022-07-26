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
    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        cinemaIndex = savedInstanceState?.getInt(KEY_CINEMA_INDEX) ?: -1
        options = intent.getParcelableExtra<Options>(KEY_OPTIONS) ?: Options.DEFAULT

        createCinemaList(binding, options.count)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
        outState.putInt(KEY_CINEMA_INDEX, cinemaIndex)
    }

    override fun onRestart() {
        super.onRestart()
        if (cinemaIndex > 0) {
            binding
        }
    }

    private fun createCinemaList(binding: ActivityMainBinding, count: Int){
        val mapIterator = image_resurces.iterator()
        val cinemaBindings = (0 until count).map {
            val cinemaBinding = ItemCinemaBinding.inflate(layoutInflater)
            cinemaBinding.root.id = View.generateViewId()
            val next = mapIterator.next()
            cinemaBinding.cinemaImageView.setImageResource(next.second)
            cinemaBinding.cinemaTitleTextView.text = next.first
            cinemaBinding.root.tag = next.second
            cinemaBinding.cinemaDetailsButton.setOnClickListener { onItemSelected(cinemaBinding.root) }
            binding.root.addView(cinemaBinding.root)
            cinemaBinding
        }
        binding.flowCinema.referencedIds = cinemaBindings.map { it.root.id }.toIntArray()
    }

    private fun onItemSelected(view: View){
        cinemaIndex = view.tag as? Int ?: -1
        if (cinemaIndex > 0) {
            val intent = Intent(this, CinemaSelection:: class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val KEY_CINEMA_INDEX = "key_cinema_index"
        const val KEY_OPTIONS = "extra_options"

        val image_resurces = sequenceOf(
            "Savage" to R.drawable.savage,
            "Avatar 2" to R.drawable.avatar_2,
            "Force Works" to R.drawable.force_works,
            "Eternal Warrior" to R.drawable.eternal_warrior,
            "Siege" to R.drawable.siege
        )
    }
}