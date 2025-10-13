package com.samuelsumbane.ssptdesktop.kclient

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
//import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

open class ClassHttpClient {
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { isLenient = true })
        }
    }
}