package com.samuelsumbane.ssptdesktop.domain.usecase

import com.samuelsumbane.ssptdesktop.kclient.BranchItem
import com.samuelsumbane.ssptdesktop.domain.repository.BranchRepository
import kotlinx.coroutines.flow.Flow


class GetBranchsUseCase(private val repo: BranchRepository) {
    operator fun invoke(): Flow<List<BranchItem>> = repo.getBranchs()
}

class AddBranchUseCase(private val repo: BranchRepository) {
    suspend operator fun invoke(branch: BranchItem) = repo.addBranch(branch)
}

class EditBranchUseCase(private val repo: BranchRepository) {
    suspend operator fun invoke(branch: BranchItem) = repo.editBranch(branch)
}

class RemoveBranchUseCase(private val repo: BranchRepository) {
    suspend operator fun invoke(branchId: Int) = repo.removeBranch(branchId)
}