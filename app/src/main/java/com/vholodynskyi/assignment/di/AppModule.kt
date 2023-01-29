package com.vholodynskyi.assignment.di

import androidx.room.Room
import com.vholodynskyi.assignment.data.local.db.AppDatabase
import com.vholodynskyi.assignment.data.remote.api.RetrofitServicesProvider
import com.vholodynskyi.assignment.data.repository.ContactsRepository
import com.vholodynskyi.assignment.domain.repository.ContactsRepositoryImpl
import com.vholodynskyi.assignment.domain.usecase.GetContactUseCase
import com.vholodynskyi.assignment.domain.usecase.GetContactsUseCase
import com.vholodynskyi.assignment.presentation.contactslist.ContactsListViewModel
import com.vholodynskyi.assignment.presentation.details.DetailsViewModel
import com.vholodynskyi.assignment.presentation.main.MainActivity
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single {
        RetrofitServicesProvider().contactsService
    }
    single {
        Room.databaseBuilder(
            this.androidContext(),
            AppDatabase::class.java,
            "app-database"
        ).build()
    }

    single {
        GetContactsUseCase(get())
    }
    single {
        GetContactUseCase(get())
    }

}