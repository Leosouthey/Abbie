package com.faithl.abbie.util

import cn.dev33.satoken.stp.StpUtil

/**
 * @author Leosouthey
 * @since 2022/4/3-16:51
 **/
object Security {

    fun loggedId() = (StpUtil.getLoginId() as String).toInt()

}