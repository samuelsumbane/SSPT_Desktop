package com.samuelsumbane.ssptdesktop

import com.samuelsumbane.ssptdesktop.data.repository.*
import com.samuelsumbane.ssptdesktop.domain.repository.BranchRepository
import com.samuelsumbane.ssptdesktop.domain.repository.ClientRepository
import com.samuelsumbane.ssptdesktop.domain.repository.ConfigRepository
import com.samuelsumbane.ssptdesktop.domain.repository.DashboardRepository
import com.samuelsumbane.ssptdesktop.domain.repository.LoginUserRepository
import com.samuelsumbane.ssptdesktop.domain.repository.NotificationRepository
import com.samuelsumbane.ssptdesktop.domain.repository.OrderRepository
import com.samuelsumbane.ssptdesktop.domain.repository.ProductCategoryRepository
import com.samuelsumbane.ssptdesktop.domain.repository.ProductRepository
import com.samuelsumbane.ssptdesktop.domain.repository.ProOwnerRepository
import com.samuelsumbane.ssptdesktop.domain.repository.SaleReportRepository
import com.samuelsumbane.ssptdesktop.domain.repository.SalesRepository
import com.samuelsumbane.ssptdesktop.domain.repository.SupplierRepository
import com.samuelsumbane.ssptdesktop.domain.repository.UserRepository
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.*
import org.koin.dsl.factory
import org.koin.dsl.module
import org.koin.dsl.single

val appModule = module {

    // Repository
    single<ClientRepository> { ClientRepositoryImpl() }
    single<ProductCategoryRepository> { ProductCategoryRepositoryImpl() }
    single<ProOwnerRepository> { ProOwnerRepositoryImpl() }
    single<ProductRepository> { ProductRepositoryImpl() }
    single<BranchRepository> { BranchRepositoryImpl() }
    single<SalesRepository> { SalesRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }
    single<OrderRepository> { OrderRepositoryImpl() }
    single<SupplierRepository> { SupplierRepositoryImpl() }
    single<ConfigRepository> { ConfigRepositoryImpl() }
    single<NotificationRepository> { NotificationRepositoryImpl() }
    single<SaleReportRepository> { SaleReportRepositoryImpl() }
    single<LoginUserRepository> { LoginUserRepositoryImpl() }

    // ViewModel (for android)
    factory { ClientViewModel(get()) }
    factory { ProductCategoryViewModel(get()) }
    factory { ProOwnerViewModel(get()) }
    factory { ProductsViewModel(get(), get(), get()) }
    factory { BranchViewModel(get()) }
    factory { SaleViewModel(get(), get()) }
    factory { UserViewModel(get()) }
    factory { OrdersViewModel(get()) }
    factory { SupplierViewModel(get()) }
    factory { ConfigViewModel(get()) }
    factory { NotificationViewModel(get()) }
    factory { LogViewModel() }
    factory { SaleReportViewModel(get()) }
    factory { UserProfileViewModel(get()) }
    factory { DashboardViewModel(get(), get()) }
    factory { LoginUserViewModel(get(), get()) }
}