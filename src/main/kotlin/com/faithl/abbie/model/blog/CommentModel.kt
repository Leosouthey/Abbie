package com.faithl.abbie.model.blog

/**
 * @author Leosouthey
 * @since 2022/4/3-1:00
 **/
data class CommentModel(
    val id: Int,
    val articleId: Int,
    val content: String,
    val author: String,
    val createdAt: String,
    val updatedAt: String
)