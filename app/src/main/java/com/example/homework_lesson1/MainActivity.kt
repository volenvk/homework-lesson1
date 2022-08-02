package com.example.homework_lesson1

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.homework_lesson1.databinding.ActivityMainBinding
import com.example.homework_lesson1.databinding.ItemCinemaBinding
import com.example.homework_lesson1.model.*
import kotlin.properties.Delegates

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private var saveData: SaveData by Delegates.notNull()

    private val resultCinemaSelectedLauncher = registerForActivityResult(CinemaSelection.Contract()) {
        if (it.result_code == RESULT_OK){
            Log.i("CinemaSelectionResult", "save state as ${it.save_state} by index: ${saveData.index}")
            saveData.states[saveData.index] = it.save_state
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        saveData = savedInstanceState?.getParcelable(KEY_CINEMA_STATES) ?: SaveData()

        createCinemaList(binding, Options.DEFAULT.count)

        binding.shareFriendsImageButton.setOnClickListener { shareFriends() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_CINEMA_STATES, saveData)
    }

    override fun onResume() {
        super.onResume()
        setCinemaTitleColor()
    }

    private fun setCinemaTitleColor() {
        if (saveData.index >= 0) {
            Log.i("setCinemaTitleColor", "cinemaIndex: ${saveData.index}")
            val id = binding.flowCinema.referencedIds[saveData.index]
            val cinemaBinding = binding.root.getViewById(id)?.tag as? ItemCinemaBinding
            cinemaBinding?.let {
                Log.d("cinemaTitleTextView", "SetTextColor: gray")
                it.cinemaTitleTextView.setTextColor(Color.GRAY)
            }
        }
    }

    private fun createCinemaList(binding: ActivityMainBinding, count: Int){
        val mapIterator = Movies.movies_posters.iterator()
        val cinemaBindings = (0 until count).map { index ->
            val cinemaBinding = ItemCinemaBinding.inflate(layoutInflater)
            cinemaBinding.root.id = View.generateViewId()
            if (mapIterator.hasNext()){
                val next = mapIterator.next()
                cinemaBinding.cinemaImageView.setImageResource(next.second)
                cinemaBinding.cinemaTitleTextView.text = next.first
                cinemaBinding.root.tag = cinemaBinding
                cinemaBinding.cinemaDetailsButton.setOnClickListener { showItemSelected(index, next) }
                binding.root.addView(cinemaBinding.root)
            } else cinemaBinding.root.tag = null
            cinemaBinding
        }
        binding.flowCinema.referencedIds = cinemaBindings.filter { it.root.tag != null }.map { it.root.id }.toIntArray()
        Log.d("createCinemaList", "referencedIds: ${binding.flowCinema.referencedIds.size}")
    }

    private fun showItemSelected(index:Int, pair: Pair<String, Int>){
        if (index >= 0) {
            saveData.index = index
            resultCinemaSelectedLauncher.launch(
                CinemaSelectionRequest(
                    cinema_info =  CinemaInfoData(pair.second, Movies.movies_info[pair.first]),
                    save_state = saveData.states[index]
                ))
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun shareFriends(){
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, "$LIKE_MESSAGE_CONTENT $URI_GOOGLE_PLAY_APPS${this.packageName}")
        sendIntent.type = "text/plain"

        val chooser = Intent.createChooser(sendIntent, title)
        if (sendIntent.resolveActivity(packageManager) != null ){
            startActivity(chooser)
        }
    }

    companion object {
        private const val LIKE_MESSAGE_CONTENT = "Мне нравиться:"
        private const val URI_GOOGLE_PLAY_APPS = "https://play.google.com/store/apps/details?id="

        private const val KEY_CINEMA_STATES = "key_cinema_states"
    }
}