package com.vholodynskyi.assignment.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.vholodynskyi.assignment.data.remote.api.RetrofitServicesProvider
import com.vholodynskyi.assignment.data.remote.api.contacts.ContactsService
import com.vholodynskyi.assignment.data.local.db.AppDatabase
import com.vholodynskyi.assignment.data.repository.ContactsRepository
import com.vholodynskyi.assignment.domain.repository.ContactsRepositoryImpl
import com.vholodynskyi.assignment.domain.usecase.GetContactUseCase
import com.vholodynskyi.assignment.domain.usecase.GetContactsUseCase
import com.vholodynskyi.assignment.presentation.contactslist.ContactsListViewModel
import com.vholodynskyi.assignment.presentation.details.DetailsViewModel
import com.vholodynskyi.assignment.util.MyLazy

object GlobalFactory: ViewModelProvider.Factory {

     private val service: ContactsService by MyLazy {
        RetrofitServicesProvider().contactsService
    }

    private val repository: ContactsRepository by MyLazy {
        ContactsRepositoryImpl(service, db)
    }
    private val useCase1: GetContactsUseCase by MyLazy {
        GetContactsUseCase(repository)
    }

    private val useCase2: GetContactUseCase by MyLazy {
        GetContactUseCase(repository)
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
            ContactsListViewModel::class.java -> ContactsListViewModel(repository, useCase1)
            DetailsViewModel::class.java -> DetailsViewModel(repository,useCase2)
            else -> throw IllegalArgumentException("Cannot create factory for ${modelClass.simpleName}")
        } as T
    }
}
