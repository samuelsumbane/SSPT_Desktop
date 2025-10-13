package com.samuelsumbane.ssptdesktop

import com.samuelsumbane.ssptdesktop.data.repository.*
import com.samuelsumbane.ssptdesktop.domain.repository.BranchRepository
import com.samuelsumbane.ssptdesktop.domain.repository.ClientRepository
import com.samuelsumbane.ssptdesktop.domain.repository.ProductCategoryRepository
import com.samuelsumbane.ssptdesktop.domain.repository.ProductRepository
import com.samuelsumbane.ssptdesktop.domain.repository.ProOwnerRepository
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.*
import org.koin.dsl.module

val appModule = module {

    // Repository
    single<ClientRepository> { ClientRepositoryImpl() }
    single<ProductCategoryRepository> { ProductCategoryRepositoryImpl() }
    single<ProOwnerRepository> { ProOwnerRepositoryImpl() }
    single<ProductRepository> { ProductRepositoryImpl() }
    single<BranchRepository> { BranchRepositoryImpl() }


    // ViewModel (for android)
    factory { ClientViewModel(get()) }
    factory { ProductCategoryViewModel(get()) }
    factory { ProOwnerViewModel(get()) }
    factory { ProductsViewModel(get(), get(), get()) }
    factory { BranchViewModel(get()) }
}