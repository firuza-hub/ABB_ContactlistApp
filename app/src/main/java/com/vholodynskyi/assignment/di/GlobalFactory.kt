package com.vholodynskyi.assignment.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.vholodynskyi.assignment.data.remote.api.RetrofitServicesProvider
import com.vholodynskyi.assignment.data.remote.api.contacts.ContactsService
import com.vholodynskyi.assignment.data.local.db.AppDatabase
import com.vholodynskyi.assignment.data.repository.ContactsRepository
import com.vholodynskyi.assignment.data.repository.ContactsRepositoryImpl
import com.vholodynskyi.assignment.ui.contactslist.ContactsListViewModel
import com.vholodynskyi.assignment.ui.details.DetailsViewModel

object GlobalFactory: ViewModelProvider.Factory {

    private val service: ContactsService by lazy {
        RetrofitServicesProvider().contactsService
    }


    private val repository: ContactsRepository by lazy {
        ContactsRepositoryImpl(service, db)
    }

    lateinit var db: AppDatabase

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-database"
        ).build()
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ContactsListViewModel::class.java -> ContactsListViewModel(repository)
            DetailsViewModel::class.java -> DetailsViewModel(repository)
            else -> throw IllegalArgumentException("Cannot create factory for ${modelClass.simpleName}")
        } as T
    }
}
