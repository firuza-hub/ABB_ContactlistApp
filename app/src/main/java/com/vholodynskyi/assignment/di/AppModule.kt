package com.vholodynskyi.assignment.di

import androidx.room.Room
import com.vholodynskyi.assignment.data.local.db.AppDatabase
import com.vholodynskyi.assignment.data.remote.api.RetrofitServicesProvider
import com.vholodynskyi.assignment.domain.usecase.GetContactUseCase
import com.vholodynskyi.assignment.domain.usecase.GetContactsUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
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

    scope(named("DetailsViewModel")) {
        scoped { GetContactUseCase(get()) }

    }

}