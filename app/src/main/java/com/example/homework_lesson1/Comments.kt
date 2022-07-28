package com.example.homework_lesson1

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework_lesson1.databinding.ActivityCommentsBinding

class Comments : BaseActivity() {

    private lateinit var binding: ActivityCommentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val recyclerView = binding.commentsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CommentsRecyclerAdapter()
    }
}