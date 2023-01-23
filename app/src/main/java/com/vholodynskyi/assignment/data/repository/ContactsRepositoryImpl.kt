package com.vholodynskyi.assignment.data.repository

import com.vholodynskyi.assignment.data.local.db.AppDatabase
import com.vholodynskyi.assignment.data.local.db.contacts.DbContact
import com.vholodynskyi.assignment.data.remote.api.contacts.ContactsService
import com.vholodynskyi.assignment.ui.contactslist.ContactModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ContactsRepositoryImpl(private val service: ContactsService, private val db: AppDatabase) : ContactsRepository {

    override suspend fun refreshDbContacts() {
        try {
            val remoteData = service.getContacts()
            refreshDbData(remoteData.results?.map { it.toDbModel() })
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun getContactDetails(id: String): Flow<ContactModel> {
        return db.userDao().getActiveContactById(id).map { it.toModel() }
    }

    override suspend fun fetchDbContacts(): Flow<List<ContactModel>> {
        val flow = db.userDao().getContacts()
        return flow.map { i -> i.filter { !it.isDeleted }.map { it.toModel() } }
    }

    override suspend fun delete(id: String) {
        db.userDao().deleteById(id)
    }
    override suspend fun repair(id: String) {
        db.userDao().repairById(id)
    }

    private suspend fun refreshDbData(data: List<DbContact>?) {
        if (data != null) {
            db.userDao().clearAllActive()
            db.userDao().addAll(data)
        }
    }
}