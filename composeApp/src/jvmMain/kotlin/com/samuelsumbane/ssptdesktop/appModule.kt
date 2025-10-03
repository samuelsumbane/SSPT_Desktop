package com.samuelsumbane.ssptdesktop

import com.samuelsumbane.ssptdesktop.data.repository.ClientRepositoryImpl
import com.samuelsumbane.ssptdesktop.domain.usecase.AddClientUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.EditClientUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetClientsUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.repository.ClientRepository
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
import org.koin.dsl.module

val appModule = module {

    // Repository
    single<ClientRepository> { ClientRepositoryImpl() }

    // Use cases
    factory { GetClientsUseCase(get()) }
    factory { AddClientUseCase(get()) }
    factory { EditClientUseCase(get()) }

    // ViewModel (for android)
    factory { ClientViewModel(get(), get(), get()) }
}