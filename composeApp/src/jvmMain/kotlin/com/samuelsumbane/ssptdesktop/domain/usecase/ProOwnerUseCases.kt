package com.samuelsumbane.ssptdesktop.domain.usecase

import androidx.lifecycle.viewmodel.compose.viewModel
import com.samuelsumbane.ssptdesktop.OwnerItem
import com.samuelsumbane.ssptdesktop.domain.usecase.repository.ProOwnerRepository
import kotlinx.coroutines.flow.Flow

class AddProOwnerUseCase(private val repo: ProOwnerRepository) {
    suspend operator fun invoke(proOwner: OwnerItem) {
        repo.addOwner(proOwner)
    }
}

class EditProOwnerUseCase(private val repo: ProOwnerRepository) {
    suspend operator fun invoke(proOwner: OwnerItem) {
        repo.editOwner(proOwner)
    }
}

class RemoveProOwnerUseCase(private val repo: ProOwnerRepository) {
    suspend operator fun invoke(proOwnerId: Int) {
        repo.removeOwner(proOwnerId)
    }
}

class GetProOwnerUseCase(private val repo: ProOwnerRepository) {
    operator fun invoke(): Flow<List<OwnerItem>> = repo.getProOwners()
}




//fun getProOwners(): Flow<List<OwnerItem>>
//suspend fun AddOwner(proOwner: OwnerItem)
//suspend fun EditOwner(proOwner: OwnerItem)
//suspend fun RemoveOwner(proOwnerId: Int)