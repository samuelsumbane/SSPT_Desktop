package com.samuelsumbane.ssptdesktop

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCORS() {
    install(CORS) {
//        allowHost("192.168.1.2:8080")
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowCredentials = true

//        anyHost()  // Only for tests
        allowHost("localhost:8080", schemes = listOf("http"))
//        allowHost("192.168.1.2:8080", schemes = listOf("http"))
        allowHeader(HttpHeaders.IfNoneMatch)
//        host("http://seu-dominio.com")
        exposeHeader(HttpHeaders.ETag)
    }
}