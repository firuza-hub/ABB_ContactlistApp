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
import kotlinx.coroutines.launch
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin

class DetailsViewModel(
    private val repository: ContactsRepository
) : BaseViewModel() {
    var id: String = ""

    //NOTE: Implemented this to test scoping to viewModel
    private val myScope = getKoin().createScope(
        "ScopeNameID", named("DetailsViewModel")
    )

    private val getContactUseCase = myScope.get<GetContactUseCase>()

    val state = MutableStateFlow(ContactDetailsState())

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
                ) { result -> state.value.contact = result }
            }
        }

    }

    fun delete() {
        viewModelScope.launch {
            if (id.isNotEmpty())
                repository.delete(id)
        }
    }
    fun save() {
        println("NEW NAME - ${state.value.contact.name}")
        viewModelScope.launch {
            if (id.isNotEmpty())
                repository.update(state.value.contact)
        }
    }

    override fun onCleared() {
        super.onCleared()
        myScope.close()
    }
}

