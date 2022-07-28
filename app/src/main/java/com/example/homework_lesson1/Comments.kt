package com.example.homework_lesson1

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework_lesson1.databinding.ActivityCommentsBinding
import com.example.homework_lesson1.databinding.LayoutInputAuthorBinding
import com.example.homework_lesson1.model.CommentData
import kotlin.properties.Delegates

class Comments : BaseActivity() {

    private lateinit var binding: ActivityCommentsBinding
    private var author by Delegates.notNull<String>()
    private var data : MutableLiveData<CommentData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        data = savedInstanceState?.getParcelable(KEY_COMMENT) ?: intent.getParcelableExtra(KEY_COMMENT)

        val recyclerView = binding.commentsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CommentsRecyclerAdapter()

        showCustomInputAlertDialog()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_COMMENT, data?.value)
    }

    private fun showCustomInputAlertDialog() {
        val authorBinding = LayoutInputAuthorBinding.inflate(layoutInflater)
        authorBinding.authorInputEditText.setText(author)

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
                author = enteredText
                updateUi()
                dialog.dismiss()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    private fun updateUi(){

    }


    private fun onToMainPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    companion object {
        const val KEY_COMMENT = "key_comment"
        private const val TITLE_DIALOG = "Представьтесь"
        private const val ALERT_AUTHOR_NAME = "Пожалуйста введите свое имя"
    }
}