package com.faithl.abbie.entity.blog

import com.faithl.abbie.entity.user.User
import com.faithl.abbie.entity.user.Users
import com.faithl.abbie.model.blog.ArticleModel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * @author Leosouthey
 * @since 2022/3/27-16:45
 **/
class Article(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<Article>(Articles)

    var title by Articles.title
    var content by Articles.content
    var author by Articles.author
    var createdAt by Articles.createdAt
    var updatedAt by Articles.updatedAt

    fun toArticleModel(): ArticleModel {
        return ArticleModel(id.value, title, content, User.findById(author)!!.name, createdAt.toString(), updatedAt.toString())
    }

}

object Articles : IntIdTable("faithl_abbie_article") {

    val title = varchar("title", 255)
    val content = text("content")
    val author = reference("author", Users)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

}