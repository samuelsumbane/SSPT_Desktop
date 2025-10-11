package com.samuelsumbane.infrastructure.modules.finance

import com.samuelsumbane.ssptdesktop.modules.IdAndStatus
import com.samuelsumbane.ssptdesktop.modules.PayableDraft
import com.samuelsumbane.ssptdesktop.repositories.finnances.PayableRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.payableRoutes() {
    route("/payables") {

        get("/all-payables") {
            val payables = PayableRepository.getAllPayables()
            call.respond(payables)
        }

        //    authenticate("auth-jwt") {
        post("/create-payable") {
            try {
                val payable = call.receive<PayableDraft>()
                PayableRepository.addPayable(payable)
                call.respond(HttpStatusCode.Created, "Conta adicionada com sucesso.")
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

//        authenticate("auth-jwt") {
            put("/pay-account") {
                try {
                    val idandstatus = call.receive<IdAndStatus>()
                    PayableRepository.payTheBill(idandstatus)
                    call.respond(HttpStatusCode.Created, "Conta paga com sucesso.")
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
//        }


    }

}