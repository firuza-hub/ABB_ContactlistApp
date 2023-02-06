package com.vholodynskyi.assignment.data.local.db.contacts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vholodynskyi.assignment.domain.model.ContactModel

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
        name = "$title $firstName $lastName".trim(),
        email = email ?: "",
        picture = photo
    )
}

