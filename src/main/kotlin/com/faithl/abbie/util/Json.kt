package com.faithl.abbie.util

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.springframework.http.HttpStatus

val gson = GsonBuilder().setPrettyPrinting().create()!!

fun buildJson(json: JsonObject.() -> Unit): String {
    return gson.toJson(JsonObject().apply(json))
}

fun buildJsonArray(vararg array: JsonObject): JsonArray {
    return JsonArray().also { json -> array.forEach { json.add(it) } }
}

fun buildJsonObject(json: JsonObject.() -> Unit): JsonObject {
    return JsonObject().apply(json)
}

fun respondJson(status: HttpStatus, message: String? = null, data: JsonObject.() -> Unit = {}): String {
    return buildJson {
        addProperty("message", message ?: status.reasonPhrase)
        addProperty("code", status.value())
        add("data", JsonObject().also(data))
    }
}