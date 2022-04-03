package com.faithl.abbie.service

import cn.dev33.satoken.stp.StpInterface
import com.faithl.abbie.entity.user.Permission
import com.faithl.abbie.entity.user.Permissions
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

/**
 * @author Leosouthey
 * @since 2022/4/2-23:41
 **/
@Component
class StpInterfaceImpl : StpInterface {

    override fun getPermissionList(loginId: Any?, loginType: String?): List<String> {
        val id = (loginId as String).toInt()
        return Permission.findPermissions(id)
    }

    override fun getRoleList(loginId: Any?, loginType: String?): List<String> {
        return ArrayList()
    }

}