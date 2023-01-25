package com.vholodynskyi.assignment.data.remote.dto.contactList

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Info(
    val page: Int,
    val results: Int,
    val seed: String,
    val version: String
)