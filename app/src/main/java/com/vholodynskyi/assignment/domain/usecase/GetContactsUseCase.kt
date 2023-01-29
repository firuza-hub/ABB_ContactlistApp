package com.vholodynskyi.assignment.domain.usecase

import com.vholodynskyi.assignment.util.NetworkResult
import com.vholodynskyi.assignment.data.repository.ContactsRepository
import com.vholodynskyi.assignment.domain.model.ContactModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetContactsUseCase(private val repo: ContactsRepository) {

    suspend operator fun invoke(): Flow<NetworkResult<List<ContactModel>>> = flow {
        try {
            emit(NetworkResult.Loading<List<ContactModel>>())
            coroutineScope {
                if(repo.isDBEmpty())
                    repo.refreshDbContacts()
                repo.fetchDbContacts().map { NetworkResult.Success(it) }.collect { emit(it) }
            }

        } catch (ex: Exception) {
            emit(NetworkResult.Exception(ex.message.toString()))
        }

    }
}