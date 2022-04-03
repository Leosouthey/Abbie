package com.faithl.abbie.entity.user

import com.faithl.abbie.model.user.UserModel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * @author Leosouthey
 * @since 2022/4/2-20:42
 **/
class User(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<User>(Users) {

        fun findByName(name: String): User? {
            return User.find { Users.name eq name }.firstOrNull()
        }

    }

    val name by Users.name
    var password by Users.password
    var email by Users.email
    val createdAt by Users.createdAt
    var updatedAt by Users.updatedAt

    fun toUserModel(): UserModel {
        return UserModel(id.value, name, email, createdAt.toString(), updatedAt.toString())
    }

}

object Users : IntIdTable("faithl_user") {

    val name = varchar("username", 24).uniqueIndex()
    val password = varchar("password", 255)
    val email = varchar("email", 255).uniqueIndex()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

}