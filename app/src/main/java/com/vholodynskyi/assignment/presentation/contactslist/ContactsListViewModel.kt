package com.vholodynskyi.assignment.presentation.contactslist

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vholodynskyi.assignment.data.repository.ContactsRepository
import com.vholodynskyi.assignment.domain.model.ContactModel
import com.vholodynskyi.assignment.domain.usecase.GetContactsUseCase
import com.vholodynskyi.assignment.presentation.base.BaseViewModel
import com.vholodynskyi.assignment.util.Event
import com.vholodynskyi.assignment.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactsListViewModel(
    private val repo: ContactsRepository,
    private val getContactsUseCase: GetContactsUseCase
) : BaseViewModel() {

    private val _contacts = MutableStateFlow<List<ContactModel>>(emptyList())
    val contacts: StateFlow<List<ContactModel>>
        get() = _contacts

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    init {
        fetchDbContacts()
    }

    fun refreshDbContacts() {
        viewModelScope.launch(Dispatchers.IO) {

            eventChannel.send(Event.ShowToaster("REFRESH"))
            try {
                _isLoading.value = true
                repo.refreshDbContacts()
            } catch (ex: Exception) {
                Log.e("REFRESH_FROM_REMOTE", ex.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteDbContact(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(id)
        }
    }


    fun undoDeleteDbContact(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.repair(id)
        }
    }

    private fun fetchDbContacts() {
        viewModelScope.launch(Dispatchers.IO) {

            getContactsUseCase().collect {
                NetworkResult.handle(it, "FETCH_LIST_LOCAL") { result ->
                    _contacts.value = result
                }
            }
        }
    }


}
