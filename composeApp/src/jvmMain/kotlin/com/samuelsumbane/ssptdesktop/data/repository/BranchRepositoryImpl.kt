package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.kclient.BranchItem
import com.samuelsumbane.ssptdesktop.domain.repository.BranchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class BranchRepositoryImpl : BranchRepository {
    val _state = MutableStateFlow(
        listOf(
            BranchItem(1, "Sede", "Chongoene")
        )
    )

    override fun getBranchs(): Flow<List<BranchItem>> = _state

    override fun addBranch(branch: BranchItem) {
        _state.value += branch
    }

    override fun editBranch(branch: BranchItem) {
        _state.value = _state.value.map {
            if (it.id == branch.id) branch else it
        }
    }

    override fun removeBranch(branchId: Int) {
        _state.value.filterNot { it.id == branchId }
    }
}