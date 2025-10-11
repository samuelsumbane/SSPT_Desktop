package com.samuelsumbane.infrastructure.modules

import com.samuelsumbane.infrastructure.repositories.LogRepository
import com.samuelsumbane.ssptdesktop.modules.LogDraft
import com.samuelsumbane.ssptdesktop.repositories.respondWithCache
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.logsRoutes() {
    route("/logs") {
        authenticate("auth-jwt") {
            get("/all") {
                val logs = LogRepository.getLogs()
                call.respondWithCache(logs)
            }

            post("/create") {
                try {
                    val logDraft = call.receive<LogDraft>()
                    LogRepository.createLog(logDraft)
                    call.respond(HttpStatusCode.Created)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

        }
    }
}