package com.vholodynskyi.assignment.data.remote.dto.contactList

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Dob(
    val age: Int,
    val date: String
)