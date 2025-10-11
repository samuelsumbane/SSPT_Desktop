package com.samuelsumbane.ssptdesktop

import com.samuelsumbane.ssptdesktop.data.repository.BranchRepositoryImpl
import com.samuelsumbane.ssptdesktop.data.repository.ClientRepositoryImpl
import com.samuelsumbane.ssptdesktop.data.repository.ProOwnerRepositoryImpl
import com.samuelsumbane.ssptdesktop.data.repository.ProductCategoryRepositoryImpl
import com.samuelsumbane.ssptdesktop.data.repository.ProductRepositoryImpl
import com.samuelsumbane.ssptdesktop.domain.repository.BranchRepository
import com.samuelsumbane.ssptdesktop.domain.repository.ProductCategoryRepository
import com.samuelsumbane.ssptdesktop.domain.repository.ProductRepository
import com.samuelsumbane.ssptdesktop.domain.usecase.AddBranchUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.AddClientUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.AddProOwnerUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.AddProductCategoryUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.AddProductsUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.ChangeProductPriceUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.EditBranchUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.EditClientUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.EditProOwnerUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.EditProductCategoryUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.EditProductUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetBranchsUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetClientsUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetProOwnerUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetProductCategoriesUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetProductsUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.RemoveBranchUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.RemoveProOwnerUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.RemoveProductCategoryUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.RemoveProductUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.repository.ClientRepository
import com.samuelsumbane.ssptdesktop.domain.usecase.repository.ProOwnerRepository
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.BranchViewModel
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ProOwnerViewModel
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ProductCategoryViewModel
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ProductsViewModel
import org.koin.dsl.module

val appModule = module {

    // Repository
    single<ClientRepository> { ClientRepositoryImpl() }
    single<ProductCategoryRepository> { ProductCategoryRepositoryImpl() }
    single<ProOwnerRepository> { ProOwnerRepositoryImpl() }
    single<ProductRepository> { ProductRepositoryImpl() }
    single<BranchRepository> { BranchRepositoryImpl() }

    // Use cases
        // Client
    factory { GetClientsUseCase(get()) }
    factory { AddClientUseCase(get()) }
    factory { EditClientUseCase(get()) }

        // Category
    factory { GetProductCategoriesUseCase(get()) }
    factory { AddProductCategoryUseCase(get()) }
    factory { EditProductCategoryUseCase(get()) }
    factory { RemoveProductCategoryUseCase(get()) }

        // Owner
    factory { GetProOwnerUseCase(get()) }
    factory { AddProOwnerUseCase(get()) }
    factory { EditProOwnerUseCase(get()) }
    factory { RemoveProOwnerUseCase(get()) }

        // Products
    factory { GetProductsUseCase(get()) }
    factory { AddProductsUseCase(get()) }
    factory { EditProductUseCase(get()) }
    factory { ChangeProductPriceUseCase(get()) }
    factory { RemoveProductUseCase(get()) }
//    factory { Remov }

        // Branch
    factory { GetBranchsUseCase(get()) }
    factory { AddBranchUseCase(get()) }
    factory { EditBranchUseCase(get()) }
    factory { RemoveBranchUseCase(get()) }

    // ViewModel (for android)
    factory { ClientViewModel(get(), get(), get()) }
    factory { ProductCategoryViewModel(get(), get(), get(), get()) }
    factory { ProOwnerViewModel(get(), get(), get(), get()) }
    factory { ProductsViewModel(get(), get(), get(), get(), get(), get(), get()) }
    factory { BranchViewModel(get(), get(), get(), get()) }
}