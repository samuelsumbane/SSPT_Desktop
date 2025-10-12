package com.samuelsumbane.ssptdesktop.modules


import com.samuelsumbane.infrastructure.repositories.LogRepository
import com.samuelsumbane.ssptdesktop.ClientItem
import com.samuelsumbane.ssptdesktop.LogDraft
import com.samuelsumbane.ssptdesktop.LogLevel
import com.samuelsumbane.ssptdesktop.repositories.ClientRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.clientsRoutes() {
    route("/clients") {
//        authenticate("auth-jwt") {
            get("/all") {
                val clients = ClientRepository.getClients()
//                call.respondWithCache(clients)
//                call.respond(HttpStatusCode.OK, clients)
                call.respond(mapOf("status" to clients))
            }

            post("/create") {
                try {
                    val client = call.receive<ClientItem>()
                    ClientRepository.createClient(client)
                    call.respond(HttpStatusCode.Created)
                    //
                    LogRepository.createLog(
                        LogDraft(
                            level = LogLevel.INFO.description,
                            message = "Registo de novo cliente",
                            module = "Clients",
                            userId = null,
                            metadataJson = null
                        )
                    )
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            put("/edit") {
                try {
                    val client = call.receive<ClientItem>()
                    ClientRepository.updateClient(client)
                    call.respond(HttpStatusCode.Created)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            delete("/delete/{id}") {
                val clientId = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException()
                val requestCode = ClientRepository.removeClient(clientId)
                when (requestCode) {
                    406 -> call.respond(HttpStatusCode.NotAcceptable, "Esse cliente não pode ser deletado.")
                    404 -> call.respond(HttpStatusCode.NotFound, "Nenhum cliente encontrado com ID fornecido.")
                    200 -> call.respond(HttpStatusCode.OK)
                }
            }
        }
//    }
}