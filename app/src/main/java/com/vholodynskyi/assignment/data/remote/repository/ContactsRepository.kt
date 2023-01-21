package com.vholodynskyi.assignment.data.remote.repository

import com.vholodynskyi.assignment.data.remote.api.dto.contactList.ApiContactResponse
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getContacts(): Flow<ApiContactResponse>
}