package com.faithl.abbie.entity.blog

import com.faithl.abbie.entity.user.User
import com.faithl.abbie.entity.user.Users
import com.faithl.abbie.model.blog.ArticleModel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Leosouthey
 * @since 2022/3/27-16:45
 **/
class Article(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<Article>(Articles) {

        val regexLink = "@*!*\\[?\\[(.*?)](\\(.*?\\)\\]?)*".toRegex()
        val regexMarkChar = "#+\\s|\\*+\\s*|>+\\s*\\S*|-+\\s|\\[+\\S]|\\[+\\s]".toRegex()
        val space = " {2,}".toRegex()

    }

    var title by Articles.title
    var content by Articles.content
    var author by Articles.author
    var cover by Articles.cover
    var createdAt by Articles.createdAt
    var updatedAt by Articles.updatedAt

    fun toArticlePatternedModel(): ArticleModel {
        val handled = content.replace(regexLink, "").replace(space, " ").replace(regexMarkChar, "").replace("\r", "")
            .replace("\n", "")
        return ArticleModel(
            id.value,
            title,
            handled.substring(0, if (handled.length > 250) 250 else handled.length),
            transaction { User.findById(author)!!.name },
            cover,
            createdAt.toString(),
            updatedAt.toString()
        )
    }

    fun toArticleModel(): ArticleModel {
        return ArticleModel(
            id.value,
            title,
            content,
            transaction { User.findById(author)!!.name },
            cover,
            createdAt.toString(),
            updatedAt.toString()
        )
    }

}

object Articles : IntIdTable("faithl_abbie_article") {

    val title = varchar("title", 255)
    val content = text("content")
    val author = reference("author", Users)
    val cover = varchar("cover", 255).nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

}