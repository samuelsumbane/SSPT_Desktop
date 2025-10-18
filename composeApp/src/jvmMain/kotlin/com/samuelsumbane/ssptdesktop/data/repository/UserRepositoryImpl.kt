package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.domain.repository.UserRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.UserItem
import com.samuelsumbane.ssptdesktop.kclient.UserItemDraft
import com.samuelsumbane.ssptdesktop.kclient.apiPath
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import jdk.jshell.Snippet

class UserRepositoryImpl : UserRepository{
    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()

    override suspend fun getUsers(): List<UserItem> {
        return try {
            kClientRepo.httpClient.get("$apiPath/users/all") {
                header(HttpHeaders.Authorization , "Bearer $token")
            }.body()
        } catch(e: Exception) {
            println("Error fetching users data: $e")
            emptyList()
        }
    }

    override suspend fun addUser(userDraft: UserItemDraft): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiPath/create", userDraft)
        return StatusAndMessage(status, message)
    }

    override suspend fun removeUser(userId: Int): StatusAndMessage {
        val (status, message) = kClientRepo.deleteRequest("$apiPath/delete/$userId")
        return StatusAndMessage(status, message)
    }
}