package com.vholodynskyi.assignment.data.remote.repository

import com.vholodynskyi.assignment.data.local.db.AppDatabase
import com.vholodynskyi.assignment.data.local.db.contacts.DbContact
import com.vholodynskyi.assignment.data.remote.api.dto.contactList.ApiContactResponse
import com.vholodynskyi.assignment.data.remote.api.contacts.ContactsService
import com.vholodynskyi.assignment.ui.contactslist.ContactModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ContactsRepositoryImpl(private val service: ContactsService, private val db: AppDatabase) :
    ContactsRepository {

    override suspend fun fetchContacts(): Flow<List<ContactModel>>  {
        val remoteData = service.getContacts()
        refreshDbData(remoteData.results?.map { it.toDbModel() })

        val flow =db.userDao().getContacts()
        return flow.map{i -> i.filter{!it.isDeleted}.map { it.toModel() }}
    }

    private suspend fun refreshDbData(data: List<DbContact>?) {
        if(data != null) {
            db.userDao().clearAllActive()
            db.userDao().addAll(data)
        }
    }
}