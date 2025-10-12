package com.samuelsumbane.infrastructure.modules


import com.samuelsumbane.infrastructure.repositories.LogRepository
import com.samuelsumbane.ssptdesktop.BranchItem
import com.samuelsumbane.ssptdesktop.LogDraft
import com.samuelsumbane.ssptdesktop.LogLevel
import com.samuelsumbane.ssptdesktop.repositories.BranchRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.branchesRoutes() {
    route("/branches") {
        authenticate("auth-jwt") {
            get("all-branches") {
                val branches = BranchRepository.getAllBranches()
                call.respond(branches)
            }
        }

        authenticate("auth-jwt") {
            post("/create-branch") {
                try {
                    val branch = call.receive<BranchItem>()
                    val status = BranchRepository.createBranch(branch)
                    when (status) {
                        101 -> call.respond(HttpStatusCode.NotAcceptable, "O pacote Lite só pode ter o máximo de 1 sucursal")
                        102 -> call.respond(HttpStatusCode.NotAcceptable, "O pacote Plus só pode ter o máximo de 5 sucursais")
                        103 -> call.respond(HttpStatusCode.NotAcceptable, "Nenhum pacote válido encontrado.")
                        201 -> call.respond(HttpStatusCode.Created, "Sucursal adicionado com sucesso.")
                        404 -> call.respond(HttpStatusCode.NotFound, "o pacote do seu sistema não foi encontrado.")
                    }
                    LogRepository.createLog(
                        LogDraft(
                            level = LogLevel.INFO.description,
                            message = "Registo de nova sucursal",
                            module = "Products",
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
        }

        authenticate("auth-jwt") {
            put("/update-branch") {
                try {
                    val branch = call.receive<BranchItem>()
                    BranchRepository.updateBranch(branch)
                    call.respond(HttpStatusCode.Created)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        authenticate("auth-jwt") {
            delete("/delete-branch") {

            }
        }
    }
}