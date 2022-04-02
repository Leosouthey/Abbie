package com.faithl.abbie.conf

import com.faithl.abbie.entity.blog.Articles
import com.faithl.abbie.entity.user.Permissions
import com.faithl.abbie.entity.user.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

/**
 * @author Leosouthey
 * @since 2022/4/2-21:15
 **/
@Component
@Configuration
class DatabaseConf {

    @Value("\${database.username}")
    lateinit var username: String

    @Value("\${database.password}")
    lateinit var password: String

    @Value("\${database.url}")
    lateinit var url: String

    @Value("\${database.driver}")
    lateinit var driver: String

    @Bean
    fun database(): Database {
        val dbUrl = url
        val dbDriver = driver
        val dbUsername = username
        val dbPassword = password
        val database = Database.connect(dbUrl, dbDriver, dbUsername, dbPassword)
        initScheme()
        return database
    }

    fun initScheme() {
        transaction {
            SchemaUtils.create(Users, Permissions)
            SchemaUtils.create(Articles)
        }
    }

}