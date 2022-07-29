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
    private var liveData : MutableLiveData<CommentItem>? = MutableLiveData()
    private var saveState: SaveStateData by Delegates.notNull()
    private var author: CommentAuthor by Delegates.notNull()

    private val resultIntent: Intent get() = Intent().apply { putExtra(EXTRA_COMMENTS, saveState) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        author = savedInstanceState?.getParcelable(KEY_AUTHOR) ?: intent.getParcelableExtra(EXTRA_AUTHOR) ?: throw IllegalArgumentException("Can't start without author")
        saveState = savedInstanceState?.getParcelable(KEY_COMMENTS) ?: intent.getParcelableExtra(EXTRA_COMMENTS) ?: SaveStateData()

        val recyclerView = binding.commentsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CommentsRecyclerAdapter(saveState.commentaries)

        liveData?.observe(this, Observer<CommentItem> { saveState.commentaries.add(it) })

        setOnCommentEditListener()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_AUTHOR, author)
        outState.putParcelable(KEY_COMMENTS, saveState)
    }

    private fun onSavePressed() {
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun onBackPressed() {
        onSavePressed()
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setOnCommentEditListener() {
        binding.cinemaCommentTextEdit.setOnKeyListener { view, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val editText = view as TextView
                val value: String = editText.text.toString()
                liveData?.postValue(CommentItem(
                    author = CommentAuthor(author.name, author.color),
                    comment = value
                ))
                editText.text = ""
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    class Contract: ActivityResultContract<CommentDataRequest, ActivityResultResponse>(){
        override fun createIntent(context: Context, input: CommentDataRequest?): Intent {
            val intent = Intent(context, Comments::class.java)
            intent.putExtra(EXTRA_AUTHOR, input?.author)
            intent.putExtra(EXTRA_COMMENTS, input?.save_state)
            return intent
        }
        override fun parseResult(resultCode: Int, intent: Intent?): ActivityResultResponse? {
            return intent?.getParcelableExtra<SaveStateData>(EXTRA_COMMENTS)?.let {
                ActivityResultResponse(it, resultCode)
            }
        }
    }

    companion object {
        private const val KEY_COMMENTS = "key_comments"
        private const val KEY_AUTHOR = "key_author"

        private const val EXTRA_COMMENTS = "extra_comments"
        private const val EXTRA_AUTHOR = "extra_author"
    }
}