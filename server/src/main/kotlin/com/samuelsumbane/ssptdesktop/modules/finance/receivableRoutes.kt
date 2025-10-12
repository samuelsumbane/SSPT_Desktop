package com.samuelsumbane.infrastructure.modules.finance

import com.samuelsumbane.ssptdesktop.IdAndStatus
import com.samuelsumbane.ssptdesktop.ReceivableDraft
import com.samuelsumbane.ssptdesktop.repositories.finnances.ReceivableRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.receivableRoutes() {
    route("/receivables") {

        get("/all-receivables") {
            val receivables = ReceivableRepository.getAllReceivables()
            call.respond(receivables)
        }

        //    authenticate("auth-jwt") {
        post("/create-receivable") {
            try {
                val receivable = call.receive<ReceivableDraft>()
                ReceivableRepository.addReceivable(receivable)
                call.respond(HttpStatusCode.Created, "Conta adicionada com sucesso.")
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
//    }


//        authenticate("auth-jwt") {
        put("/receive-account-payment") {
            try {
                val idandstatus = call.receive<IdAndStatus>()
                ReceivableRepository.receiveTheBill(idandstatus)
                call.respond(HttpStatusCode.Created, "Pagamento recebido com sucesso.")
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
//        }



    }

}