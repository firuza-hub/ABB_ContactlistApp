package com.vholodynskyi.assignment.data.remote.repository

import com.vholodynskyi.assignment.data.remote.api.dto.contactList.ApiContactResponse
import com.vholodynskyi.assignment.data.remote.api.contacts.ContactsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ContactsRepositoryImpl(private val service: ContactsService) : ContactsRepository {
    override fun getContacts(): Flow<ApiContactResponse> = flow {
        emit(service.getContacts())
    }
}