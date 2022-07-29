package com.example.homework_lesson1

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContract
import com.example.homework_lesson1.databinding.ActivityCinemaSelectionBinding
import com.example.homework_lesson1.databinding.LayoutInputAuthorBinding
import com.example.homework_lesson1.model.*
import java.util.*
import kotlin.properties.Delegates

class CinemaSelection : BaseActivity() {

    private lateinit var binding: ActivityCinemaSelectionBinding
    private var saveState: SelectedCinemaData by Delegates.notNull()

    private val resultIntent: Intent get() = Intent().apply {
        putExtra(EXTRA_SELECTED_CINEMA, SelectedCinemaData(is_like = binding.cinemaLikeCheckBox.isChecked, commentaries = saveState.commentaries))
    }

    private val resultCinemaSelectedLauncher = registerForActivityResult(Comments.Contract()) {
        if (it.result_code == RESULT_OK){
            Log.i("CommentsResult", "save comments size: ${it.commentaries.size}")
            saveState = SelectedCinemaData(it.is_like, it.commentaries)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaSelectionBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val cinemaData = intent.getParcelableExtra<CinemaData>(EXTRA_CINEMA) ?: throw IllegalArgumentException("Can't start without cinema info")
        binding.cinemaImageView.setImageResource(cinemaData.image_id ?: throw IllegalArgumentException("Can't start without cinema poster"))
        binding.cinemaAboutTextView.text = cinemaData.cinema_info ?: throw IllegalArgumentException("Can't start without cinema description")

        val selectedData = intent.getParcelableExtra<SelectedCinemaData>(EXTRA_SELECTED_CINEMA) ?: throw IllegalArgumentException("Can't start without cave state")
        binding.cinemaLikeCheckBox.isChecked = savedInstanceState?.getBoolean(KEY_IS_LIKE) ?: selectedData.is_like ?: false
        comments = savedInstanceState?.getParcelableArrayList(KEY_COMMENTS) ?: selectedData.comments ?: listOf()

        binding.addCommentButton.setOnClickListener { showCustomInputAlertDialog() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_LIKE, binding.cinemaLikeCheckBox.isChecked)
        outState.putParcelable(KEY_COMMENTS, saveState)
    }

    override fun onBackPressed() {
        onSavePressed()
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun onSavePressed() {
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun showCustomInputAlertDialog() {
        val authorBinding = LayoutInputAuthorBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle(TITLE_DIALOG)
            .setView(authorBinding.root)
            .setPositiveButton("OK", null)
            .create()
        dialog.setOnShowListener {
            authorBinding.authorInputEditText.requestFocus()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enteredText = authorBinding.authorInputEditText.text.toString()
                if (enteredText.isBlank()) {
                    authorBinding.authorInputEditText.error = ALERT_AUTHOR_NAME
                    return@setOnClickListener
                }
                resultCinemaSelectedLauncher.launch(
                    InputCommentData(
                        author = enteredText,
                        color_author = makeRandomColor(),
                        save_state = SelectedCinemaData(binding.cinemaLikeCheckBox.isChecked, saveState.commentaries)
                    )
                )
                dialog.dismiss()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    class Contract: ActivityResultContract<InputCinemaSelection, OutputCinemaSelection>(){
        override fun createIntent(context: Context, input: InputCinemaSelection?): Intent {
            val intent = Intent(context, CinemaSelection:: class.java)
            intent.putExtra(EXTRA_CINEMA, CinemaData(image_id = input?.image_id, cinema_info = input?.cinema_info))
            intent.putExtra(EXTRA_SELECTED_CINEMA, SelectedCinemaData(is_like = input?.save_state?.is_like, commentaries = input?.save_state?.commentaries))
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): OutputCinemaSelection? {
            return intent?.getParcelableExtra<SelectedCinemaData>(EXTRA_SELECTED_CINEMA).let {
                OutputCinemaSelection(
                    is_like = it?.is_like ?: false,
                    commentaries = it?.commentaries ?: listOf(),
                    result_code = resultCode
                )
            }
        }
    }

    private fun makeRandomColor(): Int{
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    companion object {
        private const val TITLE_DIALOG = "Представьтесь"
        private const val ALERT_AUTHOR_NAME = "Пожалуйста введите свое имя"

        private const val KEY_IS_LIKE = "key_is_like"
        private const val KEY_COMMENTS = "key_comments"

        private const val EXTRA_CINEMA = "extra_cinema"
        private const val EXTRA_SELECTED_CINEMA = "extra_selected_cinema"
    }
}