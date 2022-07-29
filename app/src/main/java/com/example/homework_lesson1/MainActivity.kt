package com.example.homework_lesson1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.homework_lesson1.databinding.ActivityMainBinding
import com.example.homework_lesson1.databinding.ItemCinemaBinding
import com.example.homework_lesson1.model.CinemaData
import com.example.homework_lesson1.model.InputCinemaSelection
import com.example.homework_lesson1.model.OutputCinemaSelection
import com.example.homework_lesson1.model.Options
import kotlin.properties.Delegates

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private var cinemaIndex by Delegates.notNull<Int>()
    private var cinemaSaveStates: MutableMap<Int, OutputCinemaSelection> = mutableMapOf()
    private lateinit var options: Options

    private val resultCinemaSelectedLauncher = registerForActivityResult(CinemaSelection.Contract()) {
        if (it.resultCode == RESULT_OK){
            Log.i("CinemaSelectionResult", "save like as ${it.isLike} and comments size: ${it.comments.size}")
            cinemaSaveStates[cinemaIndex] = it
        }
    }

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

    override fun onResume() {
        super.onResume()
        setCinemaTitleColor()
    }

    override fun onRestart() {
        super.onRestart()
        //recreate()
    }

    private fun setCinemaTitleColor() {
        if (cinemaIndex >= 0) {
            Log.d("setCinemaTitleColor", "cinemaIndex: $cinemaIndex")
            val id = binding.flowCinema.referencedIds[cinemaIndex]
            val cinemaBinding = binding.root.getViewById(id)?.tag as? ItemCinemaBinding
            cinemaBinding?.let {
                Log.d("cinemaTitleTextView", "SetTextColor: white")
                it.cinemaTitleTextView.setTextColor(Color.WHITE)
            }
        }
    }

    private fun createCinemaList(binding: ActivityMainBinding, count: Int){
        val mapIterator = CinemaData.movies_posters.iterator()
        val cinemaBindings = (0 until count).map { index ->
            val cinemaBinding = ItemCinemaBinding.inflate(layoutInflater)
            cinemaBinding.root.id = View.generateViewId()
            val next = mapIterator.next()
            cinemaBinding.cinemaImageView.setImageResource(next.second)
            cinemaBinding.cinemaTitleTextView.text = next.first
            cinemaBinding.root.tag = cinemaBinding
            cinemaBinding.cinemaDetailsButton.setOnClickListener { showItemSelected(index, next) }
            binding.root.addView(cinemaBinding.root)
            cinemaBinding
        }
        binding.flowCinema.referencedIds = cinemaBindings.map { it.root.id }.toIntArray()
        Log.d("createCinemaList", "referencedIds: ${binding.flowCinema.referencedIds.size}")
    }

    private fun showItemSelected(index:Int, pair: Pair<String, Int>){
        if (index >= 0) {
            cinemaIndex = index
            resultCinemaSelectedLauncher.launch(
                InputCinemaSelection(
                    image_id = pair.second,
                    cinema_info = CinemaData.movies_info[pair.first],
                    saveData = cinemaSaveStates[index]
                ))
        }
    }

    companion object {
        const val KEY_CINEMA_INDEX = "key_cinema_index"
        const val KEY_OPTIONS = "key_options"
    }
}