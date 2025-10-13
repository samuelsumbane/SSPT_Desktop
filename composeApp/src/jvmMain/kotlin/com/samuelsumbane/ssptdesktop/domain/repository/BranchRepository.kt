package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.BranchItem
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import kotlinx.coroutines.flow.Flow

interface BranchRepository {
    suspend fun getBranchs(): List<BranchItem>
    suspend fun addBranch(branch: BranchItem): StatusAndMessage
    suspend fun editBranch(branch: BranchItem): StatusAndMessage
    suspend fun removeBranch(branchId: Int): StatusAndMessage
}