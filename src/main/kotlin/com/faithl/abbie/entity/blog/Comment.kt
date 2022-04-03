package com.faithl.abbie.entity.blog

import com.faithl.abbie.entity.user.User
import com.faithl.abbie.entity.user.Users
import com.faithl.abbie.model.blog.CommentModel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Leosouthey
 * @since 2022/4/3-0:59
 **/

class Comment(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<Comment>(Comments)

    var article by Comments.article
    var content by Comments.content
    var parentId by Comments.parentId
    var author by Comments.author
    var createdAt by Comments.createdAt
    var updatedAt by Comments.updatedAt

    fun toCommentModel(): CommentModel {
        return CommentModel(
            id.value,
            article.value,
            parentId?.value,
            content,
            transaction { User.findById(author)!!.name },
            createdAt.toString(),
            updatedAt.toString()
        )
    }

}

object Comments : IntIdTable("faithl_abbie_comment") {

    val article = reference("article", Articles)
    val content = text("content")
    val parentId = reference("parentId", Comments).nullable()
    val author = reference("author", Users)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

}