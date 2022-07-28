package com.example.homework_lesson1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.example.homework_lesson1.databinding.ActivityCinemaSelectionBinding
import com.example.homework_lesson1.model.CinemaSelectionInput
import com.example.homework_lesson1.model.CinemaSelectionOutput

class CinemaSelection : BaseActivity() {

    private lateinit var binding: ActivityCinemaSelectionBinding

    private val resultIntent: Intent
        get() = Intent().apply {
            //putExtra(EXTRA_CINEMA_COMMENT, binding.cinemaCommentTextEdit.text.toString())
            putExtra(EXTRA_IS_LIKE, binding.cinemaLikeCheckBox.isChecked)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaSelectionBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val id = intent.getIntExtra(EXTRA_IMAGE_ID, -1)
        if (id < 0) throw IllegalArgumentException("Can't start without cinema poster")
        binding.cinemaImageView.setImageResource(id)
        binding.cinemaAboutTextView.setText(intent.getStringExtra(EXTRA_CINEMA_INFO) ?: throw IllegalArgumentException("Can't start without cinema description"))
        binding.cinemaLikeCheckBox.isChecked = savedInstanceState?.getBoolean(KEY_IS_LIKE) ?: intent.getBooleanExtra(EXTRA_IS_LIKE, false)
        //binding.cinemaCommentTextEdit.setText(savedInstanceState?.getString(KEY_COMMENT) ?: intent.getStringExtra(EXTRA_CINEMA_COMMENT))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_LIKE, binding.cinemaLikeCheckBox.isChecked)
        //outState.putString(KEY_COMMENT, binding.cinemaCommentTextEdit.text.toString())
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

    private fun onToMainPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    class Contract: ActivityResultContract<CinemaSelectionInput, CinemaSelectionOutput>(){
        override fun createIntent(context: Context, input: CinemaSelectionInput?): Intent {
            val intent = Intent(context, CinemaSelection:: class.java)
            intent.putExtra(EXTRA_IMAGE_ID, input?.image_id)
            intent.putExtra(EXTRA_CINEMA_INFO, input?.cinema_info)
            input?.saveData?.isLike?.let { intent.putExtra(EXTRA_IS_LIKE, it) }
            input?.saveData?.comment?.let { intent.putExtra(EXTRA_CINEMA_COMMENT, it) }
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): CinemaSelectionOutput? {
            return intent?.let {
                CinemaSelectionOutput(
                    isLike = it.getBooleanExtra(EXTRA_IS_LIKE, false),
                    comment = it.getStringExtra(EXTRA_CINEMA_COMMENT) ?: "",
                    resultCode = resultCode
                )
            }
        }
    }

    companion object {
        const val KEY_IS_LIKE = "key_is_like"
        const val KEY_COMMENT = "key_comment"

        const val EXTRA_IMAGE_ID = "extra_image_id"
        const val EXTRA_CINEMA_INFO = "extra_cinema_info"

        const val EXTRA_IS_LIKE = "extra_is_like"
        const val EXTRA_CINEMA_COMMENT = "extra_cinema_comment"
    }
}