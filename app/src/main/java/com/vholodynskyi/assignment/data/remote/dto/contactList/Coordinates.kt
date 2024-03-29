package com.vholodynskyi.assignment.data.remote.dto.contactList

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coordinates(
    val latitude: String,
    val longitude: String
)