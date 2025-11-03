package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.domain.repository.LoginUserRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.LoginRequest
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.apiUsersPath
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class LoginUserRepositoryImpl : LoginUserRepository {
    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()

    override suspend fun loginUser(loginData: LoginRequest): HttpResponse? {
        return try {
             kClientRepo.httpClient.post("$apiUsersPath/login?timezoneid=Maputo/Harare") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(loginData)
             }
        } catch (e: Exception) {
            println("Error during post request")
            null
        }
    }
}