package com.vholodynskyi.assignment.domain.model

data class ContactModel(
    val id:String,
    val name: String,
    val email: String,
    val picture: String?
){
    companion object{
        val NULL = ContactModel("", "", "", null)
    }
}
