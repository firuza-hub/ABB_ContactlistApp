package com.vholodynskyi.assignment.data.remote.repository

import com.vholodynskyi.assignment.data.remote.api.dto.contactList.ApiContactResponse
import com.vholodynskyi.assignment.ui.contactslist.ContactModel
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    suspend fun fetchContacts(): Flow<List<ContactModel>>
}