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
    private var saveState: SaveStateData by Delegates.notNull()
    private var infoData: CinemaInfoData by Delegates.notNull()

    private val resultIntent: Intent get() = Intent().apply {
        putExtra(EXTRA_SELECTED_CINEMA_STATE, SaveStateData(is_like = binding.cinemaLikeCheckBox.isChecked, commentaries = saveState.commentaries))
    }

    private val resultCinemaSelectedLauncher = registerForActivityResult(Comments.Contract()) {
        if (it.result_code == RESULT_OK){
            Log.i("CommentsResult", "save state is_like:${it.save_state.is_like} and commentaries size:${it.save_state.commentaries.size}")
            binding.cinemaLikeCheckBox.isChecked = it.save_state.is_like
            saveState = it.save_state
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaSelectionBinding.inflate(layoutInflater).also { setContentView(it.root) }

        infoData = savedInstanceState?.getParcelable(KEY_CINEMA_INFO) ?: intent.getParcelableExtra(EXTRA_CINEMA_INFO) ?: throw IllegalArgumentException("Can't start without cinema info")
        saveState = savedInstanceState?.getParcelable(KEY_CINEMA_STATE) ?: intent.getParcelableExtra(EXTRA_SELECTED_CINEMA_STATE) ?: SaveStateData()

        binding.cinemaImageView.setImageResource(infoData.image_id ?: throw IllegalArgumentException("Can't start without cinema poster"))
        binding.cinemaAboutTextView.text = infoData.description ?: throw IllegalArgumentException("Can't start without cinema description")
        binding.cinemaLikeCheckBox.isChecked = saveState.is_like

        binding.addCommentButton.setOnClickListener { showCustomInputAlertDialog() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_CINEMA_INFO, CinemaInfoData(image_id = infoData.image_id, description = infoData.description))
        outState.putParcelable(KEY_CINEMA_STATE, saveState)
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
                    CommentDataRequest(
                        author = CommentAuthor(enteredText, makeRandomColor()),
                        save_state = SaveStateData(binding.cinemaLikeCheckBox.isChecked, saveState.commentaries)
                    )
                )
                dialog.dismiss()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    class Contract: ActivityResultContract<CinemaSelectionRequest, ActivityResultResponse>(){
        override fun createIntent(context: Context, input: CinemaSelectionRequest?): Intent {
            val intent = Intent(context, CinemaSelection:: class.java)
            intent.putExtra(EXTRA_CINEMA_INFO, CinemaInfoData(image_id = input?.cinema_info?.image_id, description = input?.cinema_info?.description))
            intent.putExtra(EXTRA_SELECTED_CINEMA_STATE, input?.save_state)
            return intent
        }
        override fun parseResult(resultCode: Int, intent: Intent?): ActivityResultResponse? {
            return intent?.getParcelableExtra<SaveStateData>(EXTRA_SELECTED_CINEMA_STATE)?.let {
                ActivityResultResponse(it, resultCode)
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

        private const val KEY_CINEMA_INFO = "key_cinema_info"
        private const val KEY_CINEMA_STATE = "key_cinema_state"

        private const val EXTRA_CINEMA_INFO = "extra_cinema_info"
        private const val EXTRA_SELECTED_CINEMA_STATE = "extra_selected_cinema_state"
    }
}