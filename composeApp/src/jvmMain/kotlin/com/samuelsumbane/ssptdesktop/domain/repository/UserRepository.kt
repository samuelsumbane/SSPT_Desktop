package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.UserItem
import com.samuelsumbane.ssptdesktop.kclient.UserItemDraft

interface UserRepository {
    suspend fun getUsers(): List<UserItem>
    suspend fun addUser(userDraft: UserItemDraft): StatusAndMessage
    suspend fun removeUser(userId: Int): StatusAndMessage
//    suspend fun changeUserActiveState(userId: Int): StatusAndMessage
}