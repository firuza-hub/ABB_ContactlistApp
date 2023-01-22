package com.vholodynskyi.assignment.data.remote.api.dto.contactList

import com.squareup.moshi.JsonClass
import com.vholodynskyi.assignment.data.local.db.contacts.DbContact
import com.vholodynskyi.assignment.ui.contactslist.ContactModel

@JsonClass(generateAdapter = true)
data class ApiContact(
    val cell: String,
    val dob: Dob,
    val email: String,
    val gender: String,
    val id: Id,
    val location: Location,
    val login: Login,
    val name: Name,
    val nat: String,
    val phone: String,
    val picture: Picture,
    val registered: Registered
){

    fun toModel() = ContactModel(
        id = login.uuid,
        name = " ${name.title} ${name.first} ${name.last}",
        email = email ?: "",
        picture = picture.thumbnail
    )

    fun toDbModel() = DbContact(
        userId = login.uuid,
        title = name.title,
        firstName = name.first,
        lastName = name.last,
        email = email ?: "",
        photo = picture.medium,
        isDeleted = false
    )
}