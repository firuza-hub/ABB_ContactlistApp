package com.vholodynskyi.assignment.domain.usecase

import com.vholodynskyi.assignment.data.repository.ContactsRepository
import com.vholodynskyi.assignment.domain.model.ContactModel
import com.vholodynskyi.assignment.util.NetworkResult
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetContactUseCase(private val repo: ContactsRepository) {

    suspend operator fun invoke(id:String): Flow<NetworkResult<ContactModel>> = flow {
        try {
            emit(NetworkResult.Loading<ContactModel>())
            coroutineScope {
                repo.getContactDetails(id).map { NetworkResult.Success(it) }.collect { emit(it) }
            }

        } catch (ex: Exception) {
            emit(NetworkResult.Exception(ex.message.toString()))
        }

    }
}