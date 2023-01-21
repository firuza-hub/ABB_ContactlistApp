package com.vholodynskyi.assignment.data.remote.api.dto.contactList

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coordinates(
    val latitude: String,
    val longitude: String
)