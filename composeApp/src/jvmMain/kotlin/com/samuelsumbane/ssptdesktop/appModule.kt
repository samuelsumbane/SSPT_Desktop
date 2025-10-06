package com.samuelsumbane.ssptdesktop

import com.samuelsumbane.ssptdesktop.data.repository.ClientRepositoryImpl
import com.samuelsumbane.ssptdesktop.data.repository.ProductCategoryRepositoryImpl
import com.samuelsumbane.ssptdesktop.domain.repository.ProductCategoryRepository
import com.samuelsumbane.ssptdesktop.domain.usecase.AddClientUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.AddProductCategoryUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.EditClientUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.EditProductCategoryUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetClientsUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetProductCategoriesUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.RemoveProductCategoryUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.repository.ClientRepository
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ProductCategoryViewModel
import org.koin.dsl.module

val appModule = module {

    // Repository
    single<ClientRepository> { ClientRepositoryImpl() }
    single<ProductCategoryRepository> { ProductCategoryRepositoryImpl() }

    // Use cases
    factory { GetClientsUseCase(get()) }
    factory { AddClientUseCase(get()) }
    factory { EditClientUseCase(get()) }

    // Category
    factory { GetProductCategoriesUseCase(get()) }
    factory { AddProductCategoryUseCase(get()) }
    factory { EditProductCategoryUseCase(get()) }
    factory { RemoveProductCategoryUseCase(get()) }

    // ViewModel (for android)
    factory { ClientViewModel(get(), get(), get()) }
    factory { ProductCategoryViewModel(get(), get(), get(), get()) }
}