package com.faithl.abbie.controller.user

import cn.dev33.satoken.annotation.SaCheckLogin
import cn.dev33.satoken.stp.StpUtil
import com.faithl.abbie.entity.user.User
import com.faithl.abbie.entity.user.Users
import com.faithl.abbie.model.user.UserModel
import com.faithl.abbie.util.loggedId
import com.faithl.abbie.util.respondJson
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.util.DigestUtils
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Leosouthey
 * @since 2022/4/2-19:56
 **/
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(originPatterns = ["http://localhost:3000"])
class AuthController {

    /**
     * Login
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping("/login")
    fun login(username: String, password: String): String {
        // 加密密码
        val encryptedPsw = DigestUtils.md5DigestAsHex(password.toByteArray())
        // 查询用户
        val user = transaction {
            User.find {
                (Users.name eq username) and (Users.password eq encryptedPsw)
            }.firstOrNull()
        }
        // 返回结果
        return if (user == null) {
            respondJson(HttpStatus.UNAUTHORIZED, "login failed") {
                addProperty("reason", "username or password is wrong")
            }
        } else {
            StpUtil.login(user.id.value)
            respondJson(HttpStatus.OK, "login success") {
                addProperty("username", username)
                addProperty("token", StpUtil.getTokenValue())
            }
        }
    }

    /**
     * Logout
     * 登出
     *
     * @return
     */
    @RequestMapping("/logout")
    fun logout(): String {
        return if (StpUtil.isLogin()) {
            StpUtil.logout()
            respondJson(HttpStatus.OK, "logout success")
        } else {
            respondJson(HttpStatus.UNAUTHORIZED, "logout failed") {
                addProperty("reason", "not login")
            }
        }
    }

    /**
     * Profile
     * 查询自身信息
     *
     * @return
     */
    @RequestMapping("/profile")
    @SaCheckLogin
    fun profile(): UserModel {
        return User.findById(loggedId)!!.toUserModel()
    }

    /**
     * Profile
     * 查询自身信息
     *
     * @return
     */
    @RequestMapping("/profile/{userId}")
    @SaCheckLogin
    fun profile(@PathVariable userId: Int): UserModel {
        return User.findById(userId)!!.toUserModel()
    }

    /**
     * Register
     * 注册
     *
     */
    @RequestMapping("/register", "/reg")
    fun register() {
        // TODO
    }

}