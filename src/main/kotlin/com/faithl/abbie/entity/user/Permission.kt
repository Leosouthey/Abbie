package com.faithl.abbie.entity.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * @author Leosouthey
 * @since 2022/4/2 23:55
 **/
class Permission(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<Permission>(Permissions)

    val user by User referencedOn Permissions.user
    val permission by Permissions.permission

}
object Permissions : IntIdTable("faithl_user_permission") {

    val user = reference("user", Users)
    val permission = varchar("permission", 255)

}