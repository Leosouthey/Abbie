package com.faithl.abbie.controller.blog

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Leosouthey
 * @since 2022/3/27-0:19
 **/
@RestController
@RequestMapping("/api/v1/system")
@CrossOrigin
class HeaderController {

    @GetMapping("/status/maintenance")
    fun getMaintenanceStatus(): Boolean {
        return false
    }

    @GetMapping("/banner/title")
    fun title(): String {
        return "你好, 世界"
    }

    @GetMapping("/banner/description")
    fun description(): String {
        return "这里是 Abbie"
    }

}