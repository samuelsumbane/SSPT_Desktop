package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.BranchItem
import kotlinx.coroutines.flow.Flow

interface BranchRepository {
    fun getBranchs(): Flow<List<BranchItem>>
    fun addBranch(branch: BranchItem)
    fun editBranch(branch: BranchItem)
    fun removeBranch(branchId: Int)
}