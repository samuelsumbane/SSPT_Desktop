package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.ChangeStatusDC
import com.samuelsumbane.ssptdesktop.kclient.PasswordDraft
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.UpdateUserPersonalData
import com.samuelsumbane.ssptdesktop.kclient.UserItem
import com.samuelsumbane.ssptdesktop.kclient.UserItemDraft

interface UserRepository {
    suspend fun getUsers(): List<UserItem>
    suspend fun getUserById(userId: Int): UserItem?
    suspend fun getUsersStatus(): Pair<Int, Int>
    suspend fun addUser(userDraft: UserItemDraft): StatusAndMessage
    suspend fun editUserPersonalData(userPersonalData: UpdateUserPersonalData): StatusAndMessage
    suspend fun editUserPassword(passwordDraft: PasswordDraft): StatusAndMessage
    suspend fun removeUser(userId: Int): StatusAndMessage
    suspend fun changeUserStatus(userStatus: ChangeStatusDC): StatusAndMessage
}