package com.vholodynskyi.assignment.data.remote.dto.contactList

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Login(
    val md5: String,
    val password: String,
    val salt: String,
    val sha1: String,
    val sha256: String,
    val username: String,
    val uuid: String
)