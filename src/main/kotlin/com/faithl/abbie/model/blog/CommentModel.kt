package com.faithl.abbie.model.blog

/**
 * @author Leosouthey
 * @since 2022/4/3-1:00
 **/
data class CommentModel(
    val id: Int? = null,
    val articleId: Int,
    val parentId: Int? = null,
    val content: String,
    val author: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)