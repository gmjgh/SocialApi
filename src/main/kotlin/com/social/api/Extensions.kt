package com.social.api

import org.springframework.util.SerializationUtils
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

fun HttpServletRequest.getCookie(name: String): Cookie? = cookies?.find { name == it.name }

fun HttpServletResponse.addNewCookie(name: String, value: String, maxAge: Int): HttpServletResponse =
        apply {
            Cookie(name, value).apply {
                path = "/"
                isHttpOnly = true
                this.maxAge = maxAge
                addCookie(this)
            }
        }

fun HttpServletRequest?.deleteCookie(response: HttpServletResponse, name: String): HttpServletRequest? =
        this?.apply {
            cookies?.find { name == it.name }?.apply {
                value = ""
                path = "/"
                maxAge = 0
                response.addCookie(this)
            }
        }

fun Any.toBase64(): String {
    return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(this))
}

inline fun <reified T> String.fromBase64(): T? = T::class.java.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(this)))

inline fun <reified T> Cookie.fromBase64(): T? = value.fromBase64<T>()
