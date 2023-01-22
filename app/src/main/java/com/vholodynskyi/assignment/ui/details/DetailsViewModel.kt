package com.vholodynskyi.assignment.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vholodynskyi.assignment.data.repository.ContactsRepository
import com.vholodynskyi.assignment.ui.contactslist.ContactModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: ContactsRepository) : ViewModel() {
    var id: String = ""

    val contact = MutableStateFlow(ContactModel.NULL)


    fun getData(toast: (message: String) -> Unit) {
        if (id.isEmpty()) {
            toast("Id not valid")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getContactDetails(id).collect { contact.value = it }
            } catch (ex: Exception) {
                Log.e("GET_CONTACT", ex.message.toString())

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

