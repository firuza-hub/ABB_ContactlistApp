package com.vholodynskyi.assignment.di

import com.vholodynskyi.assignment.presentation.contactslist.ContactsListViewModel
import com.vholodynskyi.assignment.presentation.details.DetailsViewModel
import com.vholodynskyi.assignment.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel{
        ContactsListViewModel(get(), get())
    }

    viewModel{
        DetailsViewModel(get())
    }
    viewModel{
        MainViewModel(get())
    }
}