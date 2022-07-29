package com.example.homework_lesson1

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_lesson1.databinding.LayoutItemCommentBinding
import com.example.homework_lesson1.model.CommentsData

class CommentsViewHolder(itemView: LayoutItemCommentBinding) : RecyclerView.ViewHolder(itemView.root){
    val authorNameTextView: TextView = itemView.authorNameTextView
    val commentTextView: TextView = itemView.commentTextView
    val commentDateTextView: TextView = itemView.commentDateTextView
}

class CommentsRecyclerAdapter(private val comments: List<CommentsData> = listOf()): RecyclerView.Adapter<CommentsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(LayoutItemCommentBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val post = comments[position]
        holder.authorNameTextView.text = post.author.name
        holder.authorNameTextView.setTextColor(post.author.color)
        holder.commentTextView.text = post.comment
        holder.commentDateTextView.text = DateFormat.format("HH:mm dd.MM.yyyy", post.date)
    }

    override fun getItemCount(): Int = comments.size
}