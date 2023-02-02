package com.vholodynskyi.assignment.presentation.contactslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vholodynskyi.assignment.util.NetworkResult
import com.vholodynskyi.assignment.data.repository.ContactsRepository
import com.vholodynskyi.assignment.domain.model.ContactModel
import com.vholodynskyi.assignment.domain.usecase.GetContactsUseCase
import com.vholodynskyi.assignment.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContactsListViewModel(
    private val repo: ContactsRepository,
    private val getContactsUseCase: GetContactsUseCase
) : BaseViewModel() {

    private val _contacts = MutableLiveData<List<ContactModel>>()
    val contacts: LiveData<List<ContactModel>>
        get() = _contacts

    init {
        fetchDbContacts()
    }

    fun refreshDbContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.refreshDbContacts()
            } catch (ex: Exception) {
                Log.e("REFRESH_FROM_REMOTE", ex.message.toString())
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
                    _contacts.postValue(result)
                }
            }
        }
    }


}
