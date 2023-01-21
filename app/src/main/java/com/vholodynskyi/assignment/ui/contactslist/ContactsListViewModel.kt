package com.vholodynskyi.assignment.ui.contactslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vholodynskyi.assignment.di.GlobalFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ContactsListViewModel: ViewModel() {

    private val _contacts= MutableLiveData<List<ContactModel>>()
    val contacts: LiveData<List<ContactModel>>
        get() = _contacts

    private val repo = GlobalFactory.repository
    init {
        getContacts()
    }
    private fun getContacts(){
        viewModelScope.launch {
            try {
                repo.getContacts().map { r ->
                    r.results?.map { it.toModel() }
                }
                    .collect { data -> _contacts.value = data }

            } catch (ex:Exception){
                Log.e("FETCH_LIST", ex.message.toString())
            }
        }
    }
}
