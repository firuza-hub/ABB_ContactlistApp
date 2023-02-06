package com.vholodynskyi.assignment.domain.model

import com.vholodynskyi.assignment.data.local.db.contacts.DbContact

data class ContactModel(
    val id: String,
    var name: String,
    var email: String,
    val picture: String?
) {
    companion object {
        val NULL = ContactModel("", "", "", null)
        val MOCK =
            ContactModel("1", "Name", "Email", "https://randomuser.me/api/portraits/women/21.jpg")
    }

    fun toDbContact() = DbContact(
        userId = id,
        firstName = name,
        title = "",
        lastName = "",
        email = email ?: "",
        photo = picture,
        isDeleted = false
    )
}
