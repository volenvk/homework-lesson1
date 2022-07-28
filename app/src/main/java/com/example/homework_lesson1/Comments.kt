package com.example.homework_lesson1

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework_lesson1.databinding.ActivityCommentsBinding
import com.example.homework_lesson1.databinding.LayoutInputAuthorBinding
import com.example.homework_lesson1.model.CommentAuthor
import com.example.homework_lesson1.model.CommentData
import java.util.*
import kotlin.properties.Delegates

class Comments : BaseActivity() {

    private lateinit var binding: ActivityCommentsBinding
    private var author by Delegates.notNull<String>()
    private var colorAuthor by Delegates.notNull<Int>()
    private var liveData : MutableLiveData<CommentData>? = MutableLiveData()
    private var comments: MutableList<CommentData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val saveComments = savedInstanceState?.getParcelableArray(KEY_COMMENTS) ?: intent.getParcelableArrayExtra(KEY_COMMENTS)
        //comments =  saveComments

        val recyclerView = binding.commentsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CommentsRecyclerAdapter(comments)

        showCustomInputAlertDialog()

        liveData?.observe(this, Observer<CommentData> {
            comments.add(it)
        })

        binding.cinemaCommentTextEdit.setOnKeyListener { view, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val value: String = (view as TextView).text.toString()
                liveData?.postValue(CommentData(author = CommentAuthor(author, colorAuthor), comment = value))
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArray(KEY_COMMENTS, comments.toTypedArray())
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
                colorAuthor = makeRandomColor()
                updateUi()
                dialog.dismiss()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    private fun updateUi(){

    }

    private fun makeRandomColor(): Int{
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    private fun onToMainPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    companion object {
        const val KEY_COMMENTS = "key_comments"
        private const val TITLE_DIALOG = "Представьтесь"
        private const val ALERT_AUTHOR_NAME = "Пожалуйста введите свое имя"
    }
}