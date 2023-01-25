package com.vholodynskyi.assignment.presentation.details

import androidx.lifecycle.viewModelScope
import com.vholodynskyi.assignment.data.repository.ContactsRepository
import com.vholodynskyi.assignment.domain.model.ContactModel
import com.vholodynskyi.assignment.domain.usecase.GetContactUseCase
import com.vholodynskyi.assignment.presentation.base.BaseViewModel
import com.vholodynskyi.assignment.util.Event
import com.vholodynskyi.assignment.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: ContactsRepository,
    private val getContactUseCase: GetContactUseCase
) : BaseViewModel() {
    var id: String = ""

    val contact = MutableStateFlow(ContactModel.NULL)


    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (id.isEmpty()) {
                eventChannel.send(Event.ShowToaster("Id not valid"))
                return@launch
            }

            getContactUseCase(id).collect {
                NetworkResult.handle(
                    it,
                    "GET_CONTACT"
                ) { result -> contact.value = result }
            }
        }

    }

    fun delete() {
        viewModelScope.launch {
            if (id.isNotEmpty())
                repository.delete(id)
        }
    }
}
