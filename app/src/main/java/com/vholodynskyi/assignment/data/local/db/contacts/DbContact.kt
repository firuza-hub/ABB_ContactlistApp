package com.vholodynskyi.assignment.data.local.db.contacts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vholodynskyi.assignment.ui.contactslist.ContactModel
import java.util.function.UnaryOperator

@Entity(tableName = "Contact")
data class DbContact(
    @PrimaryKey val userId: String,
    @ColumnInfo val title: String?,
    @ColumnInfo val firstName: String?,
    @ColumnInfo val lastName: String?,
    @ColumnInfo val email: String?,
    @ColumnInfo val photo: String?,
    @ColumnInfo val isDeleted: Boolean,
) {
    fun toModel() = ContactModel(
        id = userId,
        name = " $title $firstName $lastName",
        email = email ?: "",
        picture = photo
    )
}

