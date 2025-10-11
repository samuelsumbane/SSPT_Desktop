package com.samuelsumbane.infrastructure.routes

import com.samuelsumbane.infrastructure.repositories.LogRepository
import com.samuelsumbane.infrastructure.repositories.ProductRepository
import com.samuelsumbane.ssptdesktop.modules.*
import com.samuelsumbane.ssptdesktop.repositories.respondWithCache
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Route.productsRoutes() {

    route("/products") {
        authenticate("auth-jwt") {
            get("/all-products") {
                val products = ProductRepository.getProducts()
                call.respondWithCache(products)
            }
        }

        authenticate("auth-jwt") {
            post("/create-product") {
                try {
                    val product = call.receive<ProductItem>()
                    ProductRepository.createProduct(product)
                    call.respond(HttpStatusCode.Created)
                    LogRepository.createLog(
                        LogDraft(
                            level = LogLevel.INFO.description,
                            message = "Registo de novo producto",
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
            put("/increase-stock") {
                try {
                    val product = call.receive<IncreaseProductStockDraft>()
                    ProductRepository.increaseStock(product)
                    call.respond(HttpStatusCode.Created, "Novo estoque adicionado com sucesso com valores actualizados.")

                    val metadata = LogMetadata(
                        productId = product.productId,
                        quantity = product.newStock,
                        userAgent = product.userId,
                        ip = null
                    )
                    val jsonMetadata = Json.encodeToString(metadata)

                    LogRepository.createLog(
                        LogDraft(
                            level = LogLevel.INFO.description,
                            message = "Modificação de estoque",
                            module = "Products",
                            userId = null,
                            metadataJson = jsonMetadata
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
            put("/change-product-price") {
                try {
                    val product = call.receive<ChangeProductPriceDraft>()
                    ProductRepository.changeProductPrice(product)
                    call.respond(HttpStatusCode.Created)
                    LogRepository.createLog(
                        LogDraft(
                            level = LogLevel.INFO.description,
                            message = "Alteração do preço do producto",
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
            put("change-product-name-and-category") {
                try {
                    val product = call.receive<ProductNameAndCategory>()
                    ProductRepository.updateProduct(product)
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