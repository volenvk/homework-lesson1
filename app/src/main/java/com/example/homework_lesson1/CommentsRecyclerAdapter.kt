package com.example.homework_lesson1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_lesson1.model.CommentData

class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val authorNameTextView: TextView = itemView.findViewById(R.id.authorNameTextView)
    val lineView: View = itemView.findViewById(R.id.lineCommentView)
    val commentTextView: TextView = itemView.findViewById(R.id.commentTextView)
}

class CommentsRecyclerAdapter(private val comments: List<CommentData> = listOf()): RecyclerView.Adapter<CommentsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_comment, parent, false))
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val post = comments[position]
        holder.authorNameTextView.text = post.author.name
        holder.authorNameTextView.setBackgroundColor(post.author.color)
        holder.commentTextView.text = post.comment
    }

    override fun getItemCount(): Int = comments.size
}