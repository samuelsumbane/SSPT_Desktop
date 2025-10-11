package com.samuelsumbane.infrastructure.routes

import com.samuelsumbane.infrastructure.repositories.SupplierRepository
import com.samuelsumbane.ssptdesktop.modules.SupplierItem
import com.samuelsumbane.ssptdesktop.repositories.respondWithCache
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.suppliersRoutes() {
    route("suppliers") {

        authenticate("auth-jwt") {
            get("/all-suppliers") {
                val suppliers = SupplierRepository.getSuppliers()
                call.respondWithCache(suppliers)
            }
        }

        authenticate("auth-jwt") {
            post("/create-supplier") {
                try {
                    val supplier = call.receive<SupplierItem>()
                    SupplierRepository.createSupplier(supplier)
                    call.respond(HttpStatusCode.Created)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        authenticate("auth-jwt") {
            put("/edit-supplier") {
                try {
                    val supplier = call.receive<SupplierItem>()
                    SupplierRepository.updateSupplier(supplier)
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