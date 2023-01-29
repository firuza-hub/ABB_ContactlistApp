package com.vholodynskyi.assignment.di

import com.vholodynskyi.assignment.data.repository.ContactsRepository
import com.vholodynskyi.assignment.domain.repository.ContactsRepositoryImpl
import org.koin.dsl.module

val repoModule = module{
    single<ContactsRepository> {
        ContactsRepositoryImpl(get(),get())
    }
}