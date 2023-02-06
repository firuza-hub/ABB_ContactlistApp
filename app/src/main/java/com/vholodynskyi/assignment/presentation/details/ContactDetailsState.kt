package com.vholodynskyi.assignment.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.vholodynskyi.assignment.domain.model.ContactModel

class ContactDetailsState {

    var contact: ContactModel = ContactModel.NULL
    var isEdit by mutableStateOf(true)
}
