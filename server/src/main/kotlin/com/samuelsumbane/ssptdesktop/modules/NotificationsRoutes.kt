package com.samuelsumbane.infrastructure.modules

import com.samuelsumbane.infrastructure.repositories.NotificationRepository
import com.samuelsumbane.ssptdesktop.modules.IdAndReadState
import com.samuelsumbane.ssptdesktop.repositories.respondWithCache
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.notificationsRoutes() {
    route("/notifications") {
        authenticate("auth-jwt") {
            get("/all") {
                val notifications = NotificationRepository.getNotifications()
                call.respondWithCache(notifications)
            }
        }

        authenticate("auth-jwt") {
            put("/update") {
                try {
                    val notification = call.receive<IdAndReadState>()
                    NotificationRepository.updateReadState(notification)
                    call.respond(HttpStatusCode.Created, "Estado da notificação actualizada com sucesso.")
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        authenticate("auth-jwt") {
            delete("/delete/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("ID invalido.")
                val delStatus = NotificationRepository.removeNotification(id)
                when (delStatus) {
                    200 -> call.respond(HttpStatusCode.OK, "Notificação deletada com sucesso.")
                    406 -> call.respond(HttpStatusCode.NotAcceptable, "Houve uma falha ao deletar o notificação. Tente novamente")
                    else -> call.respond(HttpStatusCode.BadRequest, "Houve um erro no servidor.")
                }
            }
        }
    }
}