package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.OwnerItem
import com.samuelsumbane.ssptdesktop.domain.usecase.repository.ProOwnerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ProOwnerRepositoryImpl : ProOwnerRepository{
    val _state = MutableStateFlow(
//        emptyList<OwnerItem>()
        listOf(
            OwnerItem(1, "Walter Obrain", "878293")
        )
    )

    override fun getProOwners(): Flow<List<OwnerItem>> = _state

    override suspend fun addOwner(proOwner: OwnerItem) {
        _state.value += proOwner
    }

    override suspend fun editOwner(proOwner: OwnerItem) {
        _state.value = _state.value.map {
            if (it.id == proOwner.id) proOwner else it
        }
    }

    override suspend fun removeOwner(proOwnerId: Int) {
        _state.value.filterNot { it.id == proOwnerId }
    }
}