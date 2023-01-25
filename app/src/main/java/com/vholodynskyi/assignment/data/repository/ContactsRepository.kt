package com.vholodynskyi.assignment.data.repository

import com.vholodynskyi.assignment.domain.model.ContactModel
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    suspend fun refreshDbContacts()
    suspend fun getContactDetails(id: String): Flow<ContactModel>
    suspend fun fetchDbContacts(): Flow<List<ContactModel>>
    suspend fun delete(id: String)
    suspend fun repair(id: String)
}