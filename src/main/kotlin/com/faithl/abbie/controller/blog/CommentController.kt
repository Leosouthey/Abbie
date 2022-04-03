package com.faithl.abbie.controller.blog

import cn.dev33.satoken.annotation.SaCheckLogin
import com.faithl.abbie.entity.blog.Article
import com.faithl.abbie.entity.blog.Comment
import com.faithl.abbie.entity.blog.Comments
import com.faithl.abbie.entity.user.User
import com.faithl.abbie.model.blog.CommentModel
import com.faithl.abbie.util.Security
import com.faithl.abbie.util.gson
import com.faithl.abbie.util.respondJson
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

/**
 * @author Leosouthey
 * @since 2022/4/3-1:04
 **/
@RestController
@RequestMapping("/api/v1/comment")
@CrossOrigin(originPatterns = ["http://localhost:3000"])
class CommentController {

    @RequestMapping("/create/{articleId}")
    @SaCheckLogin
    fun create(@PathVariable articleId: Int, @RequestBody comment: CommentModel): String {
        val article = transaction { Article.findById(articleId) }
        val insertedComment = transaction {
            Comment.new {
                this.article = article!!.id
                this.content = comment.content
                this.author = User.findById(Security.loggedId())!!.id
                if (comment.parentId != null) {
                    this.parentId = Comment.findById(comment.parentId)!!.id
                }
                this.createdAt = LocalDateTime.now()
                this.updatedAt = LocalDateTime.now()
            }
        }
        return respondJson(HttpStatus.OK, "create success") {
            add("comment", gson.toJsonTree(insertedComment.toCommentModel()))
        }
    }

    @RequestMapping("/delete/{commentId}")
    @SaCheckLogin
    fun delete(@PathVariable commentId: Int): String {
        val comment = transaction { Comment.findById(commentId) }
        return if (comment != null) {
            if (comment.author.value == Security.loggedId()) {
                transaction {
                    comment.delete()
                }
                respondJson(HttpStatus.OK, "delete success")
            } else {
                respondJson(HttpStatus.FORBIDDEN, "you are not author")
            }
        } else {
            respondJson(HttpStatus.NOT_FOUND, "comment not found")
        }
    }

    @RequestMapping("/update/{commentId}")
    @SaCheckLogin
    fun update(@PathVariable commentId: Int, content: String): String {
        val comment = transaction { Comment.findById(commentId) }
        return if (comment != null) {
            transaction {
                comment.content = content
                comment.updatedAt = LocalDateTime.now()
            }
            respondJson(HttpStatus.OK, "update success")
        } else {
            respondJson(HttpStatus.NOT_FOUND, "comment not found")
        }
    }

    @RequestMapping("/list/{articleId}")
    fun list(@PathVariable articleId: Int) {
        val article = transaction { Article.findById(articleId) }
        if (article != null) {
            val comments = transaction {
                Comment.find { Comments.article eq articleId }.toList()
            }
            respondJson(HttpStatus.OK, "list success") {
                add("comments", gson.toJsonTree(comments.map { it.toCommentModel() }))
            }
        } else {
            respondJson(HttpStatus.NOT_FOUND, "article not found")
        }
    }

}