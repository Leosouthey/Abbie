package com.faithl.abbie.entity.blog

import com.faithl.abbie.model.blog.CommentModel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * @author Leosouthey
 * @since 2022/4/3-0:59
 **/

class Comment(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<Comment>(Comments)

    val article by Comments.article
    var content by Comments.content
    var author by Comments.author
    val createdAt by Comments.createdAt
    var updatedAt by Comments.updatedAt

    fun toCommentModel(): CommentModel {
        return CommentModel(id.value, article.value, content, author, createdAt.toString(), updatedAt.toString())
    }

}

object Comments : IntIdTable("faithl_abbie_article") {

    val article = reference("id", Articles)
    val content = text("content")
    val author = varchar("author", 255)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

}