package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.LoginRequest
import io.ktor.client.statement.*

interface LoginUserRepository {
    suspend fun loginUser(loginData: LoginRequest): HttpResponse?
}