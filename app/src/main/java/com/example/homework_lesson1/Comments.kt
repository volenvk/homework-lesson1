package com.example.homework_lesson1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework_lesson1.databinding.ActivityCommentsBinding
import com.example.homework_lesson1.model.*
import kotlin.properties.Delegates

class Comments : BaseActivity() {

    private lateinit var binding: ActivityCommentsBinding
    private var author by Delegates.notNull<String>()
    private var colorAuthor by Delegates.notNull<Int>()
    private var liveData : MutableLiveData<CommentData>? = MutableLiveData()
    private var comments: MutableList<CommentData> = mutableListOf()

    private val resultIntent: Intent get() = Intent().apply { putExtra(CinemaSelection.EXTRA_CINEMA_COMMENTS, comments.toTypedArray()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        author = savedInstanceState?.getString(KEY_AUTHOR) ?: intent.getStringExtra(EXTRA_AUTHOR) ?: throw IllegalArgumentException("Can't start without author")
        colorAuthor = savedInstanceState?.getInt(KEY_COLOR_AUTHOR) ?: intent?.getIntExtra(EXTRA_COLOR_AUTHOR, -1)?.takeIf { it > 0 } ?: throw IllegalArgumentException("Can't start without color author")

        (savedInstanceState?.getParcelableArrayList<CommentData>(KEY_COMMENTS) ?: intent.getParcelableArrayListExtra(KEY_COMMENTS))?.let {
            comments = it.toMutableList()
        }

        val recyclerView = binding.commentsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CommentsRecyclerAdapter(comments)

        liveData?.observe(this, Observer<CommentData> {
            comments.add(it)
        })

        binding.cinemaCommentTextEdit.setOnKeyListener { view, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val editText = view as TextView
                val value: String = editText.text.toString()
                liveData?.postValue(CommentData(author = CommentAuthor(author, colorAuthor), comment = value))
                editText.text = ""
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_AUTHOR, author)
        outState.putInt(KEY_COLOR_AUTHOR, colorAuthor)
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

    private fun onToMainPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    class Contract: ActivityResultContract<InputCommentData, OutputCommentData>(){
        override fun createIntent(context: Context, input: InputCommentData?): Intent {
            val intent = Intent(context, CinemaSelection:: class.java)
            input?.author?.let { intent.putExtra(EXTRA_AUTHOR, it)}
            input?.colorAuthor?.let {  intent.putExtra(EXTRA_COLOR_AUTHOR, it) }
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): OutputCommentData? {
            return intent?.let {
                OutputCommentData(
                    comments = it.getParcelableArrayListExtra<CommentData>(EXTRA_COMMENTS)?.toList() ?: listOf(),
                    resultCode = resultCode
                )
            }
        }
    }

    companion object {
        const val KEY_COMMENTS = "key_comments"
        const val KEY_AUTHOR = "key_author"
        const val KEY_COLOR_AUTHOR = "key_color_author"

        const val EXTRA_AUTHOR = "extra_author"
        const val EXTRA_COLOR_AUTHOR = "extra_color_author"
        const val EXTRA_COMMENTS = "extra_comments"
    }
}