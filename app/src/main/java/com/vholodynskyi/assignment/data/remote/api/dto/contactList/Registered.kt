package com.vholodynskyi.assignment.data.remote.api.dto.contactList

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Registered(
    val age: Int,
    val date: String
)