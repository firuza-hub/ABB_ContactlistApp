package com.vholodynskyi.assignment.ui.contactslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vholodynskyi.assignment.data.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContactsListViewModel(private val repo: ContactsRepository): ViewModel() {

    private val _contacts= MutableLiveData<List<ContactModel>>()
    val contacts: LiveData<List<ContactModel>>
        get() = _contacts

    init {
        fetchDbContacts()
    }

    fun refreshDbContacts(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.refreshDbContacts()
            } catch (ex:Exception){
                Log.e("REFRESH_FROM_REMOTE", ex.message.toString())
            }
        }
    }

    fun deleteDbContact(id:String){
        viewModelScope.launch {
            repo.delete(id)
        }
    }


    fun undoDeleteDbContact(id:String){
        viewModelScope.launch {
            repo.repair(id)
        }
    }

    private fun fetchDbContacts(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if(repo.isDBEmpty())
                    repo.refreshDbContacts()

                repo.fetchDbContacts()
                    .collect { data -> _contacts.postValue(data) }


            } catch (ex:Exception){
                Log.e("FETCH_LIST_LOCAL", ex.message.toString())
            }
        }
    }
}
