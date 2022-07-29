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
    private var liveData : MutableLiveData<CommentItem>? = MutableLiveData()
    private var comments: MutableList<CommentsData> = mutableListOf()

    private val resultIntent: Intent get() = Intent().apply {
        putParcelableArrayListExtra(COMMENTS_EXTRA_COMMENTS, comments.toTypedArray().toCollection(ArrayList()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        author = savedInstanceState?.getString(COMMENTS_KEY_AUTHOR) ?: intent.getStringExtra(COMMENTS_EXTRA_AUTHOR) ?: throw IllegalArgumentException("Can't start without author")
        colorAuthor = savedInstanceState?.getInt(COMMENTS_KEY_COLOR_AUTHOR) ?: intent?.getIntExtra(COMMENTS_EXTRA_COLOR_AUTHOR, 0)?.takeIf { it != 0 } ?: throw IllegalArgumentException("Can't start without color author")

        (savedInstanceState?.getParcelableArrayList<CommentsData>(COMMENTS_KEY_COMMENTS) ?: intent.getParcelableArrayListExtra(COMMENTS_EXTRA_COMMENTS))?.let {
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
                liveData?.postValue(CommentItem(author = CommentAuthor(author, colorAuthor), comment = value))
                editText.text = ""
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_COMMENTS, CommentData(author = CommentAuthor(name= author, color = colorAuthor), comments = comments))
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
            val intent = Intent(context, Comments:: class.java)
            intent.putExtra(EXTRA_COMMENTS, CommentsData(author = CommentAuthor(name= input?.author, color = input?.color_author), comments = input?.save_state?.commentaries))
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): OutputCommentData? {
            return intent?.getParcelableExtra<CommentsData>(EXTRA_COMMENTS)?.let {
                OutputCommentData(
                    commentaries = it.comments ?: listOf(),
                    result_code = resultCode
                )
            }
        }
    }

    companion object {
        private const val KEY_COMMENTS = "key_comments"
        private const val EXTRA_COMMENTS = "extra_comments"
    }
}