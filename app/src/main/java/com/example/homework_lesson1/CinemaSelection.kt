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

class CinemaSelection : BaseActivity() {

    private lateinit var binding: ActivityCinemaSelectionBinding
    private var comments: List<CommentData> = listOf()

    private val resultIntent: Intent
        get() = Intent().apply {
            putExtra(EXTRA_CINEMA_COMMENTS, comments.toTypedArray())
            putExtra(EXTRA_IS_LIKE, binding.cinemaLikeCheckBox.isChecked)
        }

    private val resultCinemaSelectedLauncher = registerForActivityResult(Comments.Contract()) {
        if (it.resultCode == RESULT_OK){
            Log.i("CommentsResult", "save comments size: ${it.comments.size}")
            comments = it.comments
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaSelectionBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val id = intent.getIntExtra(EXTRA_IMAGE_ID, -1)
        if (id < 0) throw IllegalArgumentException("Can't start without cinema poster")
        binding.cinemaImageView.setImageResource(id)
        binding.cinemaAboutTextView.setText(intent.getStringExtra(EXTRA_CINEMA_INFO) ?: throw IllegalArgumentException("Can't start without cinema description"))
        binding.cinemaLikeCheckBox.isChecked = savedInstanceState?.getBoolean(KEY_IS_LIKE) ?: intent.getBooleanExtra(EXTRA_IS_LIKE, false)

        binding.addCommentButton.setOnClickListener { showCustomInputAlertDialog() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_LIKE, binding.cinemaLikeCheckBox.isChecked)
        outState.putParcelableArray(KEY_COMMENTS, comments.toTypedArray())
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
                resultCinemaSelectedLauncher.launch(InputCommentData(author = enteredText, colorAuthor = makeRandomColor()))
                dialog.dismiss()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    class Contract: ActivityResultContract<InputCinemaSelection, OutputCinemaSelection>(){
        override fun createIntent(context: Context, input: InputCinemaSelection?): Intent {
            val intent = Intent(context, CinemaSelection:: class.java)
            intent.putExtra(EXTRA_IMAGE_ID, input?.image_id)
            intent.putExtra(EXTRA_CINEMA_INFO, input?.cinema_info)
            input?.saveData?.isLike?.let { intent.putExtra(EXTRA_IS_LIKE, it) }
            input?.saveData?.comments?.let { intent.putExtra(EXTRA_CINEMA_COMMENTS, it.toTypedArray()) }
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): OutputCinemaSelection? {
            return intent?.let {
                OutputCinemaSelection(
                    isLike = it.getBooleanExtra(EXTRA_IS_LIKE, false),
                    comments = it.getParcelableArrayListExtra<CommentData>(EXTRA_CINEMA_COMMENTS)?.toList() ?: listOf(),
                    resultCode = resultCode
                )
            }
        }
    }

    private fun makeRandomColor(): Int{
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    companion object {
        const val KEY_IS_LIKE = "key_is_like"
        const val KEY_COMMENTS = "key_comments"
        const val EXTRA_IMAGE_ID = "extra_image_id"
        const val EXTRA_CINEMA_INFO = "extra_cinema_info"
        private const val TITLE_DIALOG = "Представьтесь"
        private const val ALERT_AUTHOR_NAME = "Пожалуйста введите свое имя"
        const val EXTRA_IS_LIKE = "extra_is_like"
        const val EXTRA_CINEMA_COMMENTS = "extra_cinema_comments"
    }
}