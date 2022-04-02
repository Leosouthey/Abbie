package com.faithl.abbie.controller.blog

import cn.dev33.satoken.stp.StpUtil
import com.faithl.abbie.entity.blog.Article
import com.faithl.abbie.entity.blog.Articles
import com.faithl.abbie.model.blog.ArticleModel
import com.faithl.abbie.util.gson
import com.faithl.abbie.util.respondJson
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
@CrossOrigin(originPatterns = ["http://localhost:3000"])
class Article {

    @GetMapping("/list")
    fun list(): List<ArticleModel> {
        return transaction {
            Article.all().map {
                it.toArticleModel()
            }
        }
    }

    @PostMapping("/create")
    fun create(@RequestBody article: ArticleModel): String {
        if (!StpUtil.isLogin()) {
            return respondJson(HttpStatus.UNAUTHORIZED, "create failed") {
                addProperty("reason", "you are not login")
            }
        }
        if (!StpUtil.hasPermission("article-create")) {
            return respondJson(HttpStatus.UNAUTHORIZED, "create failed") {
                addProperty("reason", "you have no permission")
            }
        }
        return transaction {
            val result = Article.new {
                title = article.title
                content = article.content
                author = article.author.toString()
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
    fun update(@PathVariable id: String, @RequestBody article: ArticleModel): String {
        if (!StpUtil.isLogin()) {
            return respondJson(HttpStatus.UNAUTHORIZED, "update failed") {
                addProperty("reason", "you are not login")
            }
        }
        if (!StpUtil.hasPermission("article-update")) {
            return respondJson(HttpStatus.UNAUTHORIZED, "update failed") {
                addProperty("reason", "you have no permission")
            }
        }
        val result = transaction { Article.find(Articles.id eq id.toInt()).firstOrNull() }
        return if (result == null) {
            respondJson(HttpStatus.NOT_FOUND, "update failed") {
                addProperty("reason", "article not found")
            }
        } else {
            transaction {
                result.title = article.title
                result.content = article.content
                result.author = article.author.toString()
                result.updatedAt = LocalDateTime.now()
            }
            respondJson(HttpStatus.OK, "update success") {
                add("article", gson.toJsonTree(result.toArticleModel()))
            }
        }
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: String): String {
        if (!StpUtil.isLogin()) {
            return respondJson(HttpStatus.UNAUTHORIZED, "delete failed") {
                addProperty("reason", "you are not login")
            }
        }
        if (!StpUtil.hasPermission("article-delete")) {
            return respondJson(HttpStatus.UNAUTHORIZED, "delete failed") {
                addProperty("reason", "you have no permission")
            }
        }
        val result = transaction { Article.find(Articles.id eq id.toInt()).firstOrNull() }
        return if (result == null) {
            respondJson(HttpStatus.NOT_FOUND, "delete failed") {
                addProperty("reason", "article not found")
            }
        } else {
            transaction { result.delete() }
            respondJson(HttpStatus.OK, "delete success") {
                add("article", gson.toJsonTree(result.toArticleModel()))
            }
        }
    }

}