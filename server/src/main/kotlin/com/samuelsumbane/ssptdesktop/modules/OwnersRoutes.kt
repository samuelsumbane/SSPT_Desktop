package com.samuelsumbane.infrastructure.routes

import com.samuelsumbane.infrastructure.repositories.ProductOwnerRepository
import com.samuelsumbane.ssptdesktop.OwnerItem
import com.samuelsumbane.ssptdesktop.repositories.respondWithCache
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.ownersRoutes() {

    route("/owners") {
        authenticate("auth-jwt") {
            get("/all-owners") {
                val owners = ProductOwnerRepository.getOwners()
                call.respondWithCache(owners)
            }
        }

        authenticate("auth-jwt") {
            post("/create-owner") {
                try {
                    val owner = call.receive<OwnerItem>()
                    val status = ProductOwnerRepository.createOwner(owner)
                    when (status) {
                        101 -> call.respond(HttpStatusCode.NotAcceptable, "O pacote Lite só pode ter o máximo de 1 proprietário")
                        102 -> call.respond(HttpStatusCode.NotAcceptable, "O pacote Plus só pode ter o máximo de 5 proprietários")
                        103 -> call.respond(HttpStatusCode.NotAcceptable, "Nenhum pacote válido encontrado.")
                        201 -> call.respond(HttpStatusCode.Created, "proprietário adicionado com sucesso.")
                        404 -> call.respond(HttpStatusCode.NotFound, "o pacote do seu sistema não foi encontrado.")
                    }
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        authenticate("auth-jwt") {
            put("/edit-owner") {
                try {
                    val owner = call.receive<OwnerItem>()
                    val statusCode = ProductOwnerRepository.updateOwner(owner)
                    when (statusCode) {
                        201 -> call.respond(HttpStatusCode.Created, "Dados do proprietário actualizado com sucesso.")
                        404 -> call.respond(HttpStatusCode.NotFound, "Nenhum proprietário encontrado com ID fornecido.")
                        else -> call.respond(HttpStatusCode.BadRequest, "Houve no servidor. Tenta actualizar a pagina e tentar novamente.")
                    }
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        delete("/delete-owner/{id}") {
            val ownerId = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException()
            val requestCode = ProductOwnerRepository.removeOwner(ownerId)
            when (requestCode) {
                406 -> call.respond(HttpStatusCode.NotAcceptable, "O proprietário já tem dados no sistema e não pode ser deletado.")
                404 -> call.respond(HttpStatusCode.NotFound, "Nenhum proprietário encontrado com ID fornecido.")
                200 -> call.respond(HttpStatusCode.OK, "Proprietário deletado com sucesso.")
            }
        }
    }

}