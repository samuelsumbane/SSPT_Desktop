package com.samuelsumbane.ssptdesktop.kclient

import com.samuelsumbane.ssptdesktop.ui.utils.ConnectionType
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.net.ConnectException

class KClientRepository : ClassHttpClient() {
    suspend inline fun <reified T : Any> postRequest(
        url: String,
        data: T,
        method: String = "post"
    ): Pair<Int, String> {
        return try {
//            val token = sessionStorage.getItem("jwt_token") ?: ""
            val token = Session.jwtToken ?: ""
            val response = when (method.lowercase()) {
                "post" -> httpClient.post(url) {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.Authorization, "Bearer $token")
                    setBody(data)
                }
                "put" -> httpClient.put(url) {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.Authorization, "Bearer $token")
                    setBody(data)
                }
                else -> throw IllegalArgumentException("Unsupported HTTP method: $method")
            }
            Pair(response.status.value, response.bodyAsText())
        } catch (e: Exception) {
            println("Error during POST/PUT: ${e.message}")
            Pair(400, "")
        }
    }

//    suspend inline fun <reified T : Any> postRequestReturnIntAndString(
//        url: String,
//        data: T,
//        method: String = "post"
//    ): Pair<HttpStatusCode, IntAndStringValues> {
//        return try {
//            val token = sessionStorage.getItem("jwt_token") ?: ""
//            val response = if (method.lowercase() == "post") {
//                httpClient.post(url) {
//                    contentType(ContentType.Application.Json)
//                    header(HttpHeaders.Authorization, "Bearer $token")
//                    setBody(data)
//                }
//            } else throw IllegalArgumentException("Unsupported HTTP method: $method")
//
//            Pair(response.status, IntAndStringValues(intValue = response.status.value, stringValue = response) )
//        } catch (e: Exception) {
//            println("Error during POST/PUT: ${e.message}")
//            Pair(HttpStatusCode.BadRequest, IntAndStringValues(intValue = 400, stringValue = "") )
//
//        }
//    }

    suspend fun deleteRequest(url: String): Pair<Int, String> {
        return try {
            val token = Session.jwtToken ?: ""
            val response = httpClient.delete(url) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            Pair(response.status.value, response.bodyAsText())
        } catch (e: Exception) {
            Pair(400, "")
        }
    }

    suspend fun checkConnection(): ConnectionType {
        return try {
            val response = httpClient.get("$apiPath") {
                header(HttpHeaders.Authorization, "Bearer")
            }
            ConnectionType.Stabilished
        } catch(e: ConnectException) {
            ConnectionType.Refused
        }
    }

}