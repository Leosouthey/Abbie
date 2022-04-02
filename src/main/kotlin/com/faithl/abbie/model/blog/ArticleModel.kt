package com.faithl.abbie.model.blog

/**
 * @author Leosouthey
 * @since 2022/4/3-0:55
 **/
data class ArticleModel(
    val id: Int? = null,
    val title: String,
    val content: String,
    val author: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)