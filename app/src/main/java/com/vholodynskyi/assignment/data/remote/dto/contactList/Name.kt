package com.vholodynskyi.assignment.data.remote.dto.contactList

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Name(
    val first: String,
    val last: String,
    val title: String
)