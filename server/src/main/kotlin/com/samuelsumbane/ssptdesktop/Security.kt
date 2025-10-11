package com.samuelsumbane.ssptdesktop

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
//import com.samuelsumbane.infrastructure.repositories.tokkens.TokenStore
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


const val SECRET = "meu_super_secreto"
const val ISSUER = "ktor-server"
const val AUDIENCE = "ktor-client"

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") {
//            realm = this@configureSecurity.realm
            verifier(
                JWT.require(Algorithm.HMAC256(SECRET))
                    .withIssuer(ISSUER)
                    .withAudience(AUDIENCE)
                    .build()
            )

            validate { credential ->
                val email = credential.payload.getClaim("username").asString()
                val tokenId = credential.payload.getClaim("tokenId").asString()

//                if (email != null && tokenId != null && TokenStore.isTokenActive(tokenId)) {
//                    JWTPrincipal(credential.payload)
//                } else null
            }
        }
    }
}