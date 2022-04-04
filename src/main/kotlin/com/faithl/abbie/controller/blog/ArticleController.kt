package com.faithl.abbie.controller.blog

import cn.dev33.satoken.annotation.SaCheckLogin
import cn.dev33.satoken.annotation.SaCheckPermission
import com.faithl.abbie.entity.blog.Article
import com.faithl.abbie.entity.blog.Articles
import com.faithl.abbie.entity.user.User
import com.faithl.abbie.model.blog.ArticleModel
import com.faithl.abbie.util.Security
import com.faithl.abbie.util.gson
import com.faithl.abbie.util.respondJson
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

/**
 * @author Leosouthey
 * @since 2022/4/2-23:34
 **/
@RestController
@RequestMapping("/api/v1/article")
@CrossOrigin
class ArticleController {

    @GetMapping("/list")
    fun list(): Int {
        return transaction {
            Article.all().count().toInt()
        }
    }

    @GetMapping("/list/{sort}")
    fun list(@PathVariable sort: String, limit: Int, offset: Int): List<ArticleModel> {
        return if (sort == "update") {
            transaction {
                Article.all().orderBy(Articles.updatedAt to SortOrder.DESC).limit(limit, offset.toLong()).map {
                    it.toArticlePatternedModel()
                }
            }
        } else {
            transaction {
                Article.all().orderBy(Articles.id to SortOrder.ASC).limit(limit, offset.toLong()).map {
                    it.toArticlePatternedModel()
                }
            }
        }
    }

    @PostMapping("/create")
    @SaCheckLogin
    @SaCheckPermission("article-create")
    fun create(@RequestBody article: ArticleModel): String {
        return transaction {
            val result = Article.new {
                title = article.title
                content = article.content
                author = User.findById(Security.loggedId())!!.id
                createdAt = LocalDateTime.now()
                updatedAt = LocalDateTime.now()
            }
            respondJson(HttpStatus.OK, "create success") {
                add("article", gson.toJsonTree(result.toArticleModel()))
            }
        }
    }

    @GetMapping("/detail/{id}")
    fun detail(@PathVariable id: Int): ArticleModel? {
        return transaction { Article.findById(id)?.toArticleModel() }
    }

    @PutMapping("/update/{id}")
    @SaCheckLogin
    @SaCheckPermission("article-update")
    fun update(@PathVariable id: Int, @RequestBody article: ArticleModel): String {
        val result = transaction { Article.findById(id) }
        return if (result == null) {
            respondJson(HttpStatus.NOT_FOUND, "update failed") {
                addProperty("reason", "article not found")
            }
        } else {
            transaction {
                result.title = article.title
                result.content = article.content
                result.author = User.findById(Security.loggedId())!!.id
                result.updatedAt = LocalDateTime.now()
            }
            respondJson(HttpStatus.OK, "update success") {
                add("article", gson.toJsonTree(result.toArticleModel()))
            }
        }
    }

    @DeleteMapping("/delete/{id}")
    @SaCheckLogin
    @SaCheckPermission("article-delete")
    fun delete(@PathVariable id: Int): String {
        val result = transaction { Article.findById(id) }
        return if (result == null) {
            respondJson(HttpStatus.NOT_FOUND, "delete failed") {
                addProperty("reason", "article not found")
            }
        } else {
            transaction { result.delete() }
            respondJson(HttpStatus.OK, "delete success")
        }
    }

}