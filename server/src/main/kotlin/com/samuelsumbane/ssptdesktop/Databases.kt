package com.samuelsumbane.ssptdesktop


//import io.ktor.server.plugins.contentnegotiation.*
import com.samuelsumbane.infrastructure.repositories.*
import com.samuelsumbane.infrastructure.repositories.OrderRepository.generateOrdersCsv
import com.samuelsumbane.infrastructure.repositories.OrderRepository.generateOrdersItemsExcel
import com.samuelsumbane.ssptdesktop.repositories.ReportRepository
import com.samuelsumbane.ssptdesktop.repositories.getCurrentTimestamp
import com.samuelsumbane.ssptdesktop.repositories.longTimeToString
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
//import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.FileNotFoundException
import java.util.*


fun Application.configureDatabases() {
    val props = Properties().apply {
        val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream("database.properties")
            ?: throw FileNotFoundException("Arquivo database.properties não encontrado em resources.")
        load(inputStream)
    }

    val url = props.getProperty("jdbc.url")
    val user = props.getProperty("jdbc.user")
    val password = props.getProperty("jdbc.password")
    val driver = props.getProperty("jdbc.driver")

    // Conecta ao banco usando os dados do arquivo
    Database.connect(
        url = url,
        driver = driver,
        user = user,
        password = password
    )


    // Create tables ---------->>
    transaction {
        create(
            Categories, Clients, Products,
            StockMovements, Suppliers, Orders,
            OrdersItems, SysConfigurations, Users,
            SysBranches, payables, receivables,
            Owners, Notifications, Logs,
            PasswordResetCodes
        )
    }

    routing {
        val currentDateTime = longTimeToString(getCurrentTimestamp())

        // Orders ------------->>
        authenticate("auth-jwt") {
            get("/orders") {
                val orders = OrderRepository.getOrders()
                call.respond(orders)
            }
        }


        authenticate("auth-jwt") {
            get("/orders-items") {
                val ordersItems = OrderItemRepository.getOrdersItems()
                call.respond(ordersItems)
            }
        }


        route("/order") {
            // To Excel file -------->>
            get("/export/orders") {
                val orders = ReportRepository.allReports()
                val fileBytes = generateOrdersItemsExcel(orders)

                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "orders_items-$currentDateTime.xlsx").toString()
                )
                val excelContentType = ContentType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                call.respondBytes(fileBytes, excelContentType)
            }


            get("/export/orders/csv") {
                val ordersItems = ReportRepository.allReports()
                val fileBytes = generateOrdersCsv(ordersItems)

                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "orders_items-$currentDateTime.csv").toString()
                )
                call.respondBytes(fileBytes, ContentType.Text.CSV)
            }

            get("/export/orders/json") {
                val ordersitems = ReportRepository.allReports()
                val json = Json.encodeToString(ordersitems)

                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "orders_items-$currentDateTime.json").toString()
                )
                call.respondText(json, ContentType.Application.Json)
            }

            //

            get("/order-items-by-id/{id}") {
                val idParam = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID")
                try {
                    val id = UUID.fromString(idParam) // Converte a String para UUID
                    val ordersItems = OrderItemRepository.getOrdersItemsById(id)

                    call.respond(ordersItems)

                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid UUID format")
                }
            }

            authenticate("auth-jwt") {
                post("/sale_products") {
                    val products = call.receive<SaleItem>()
                    try {
                        SaleRepository.saleProduct(products)
                        call.respond(HttpStatusCode.Created, "Venda feita com sucesso.")

                        val sellMetadata = SellMetadata(
                            value = products.order.total,
                        )
                        val stringSellMetadata = Json.encodeToString(sellMetadata)

                        LogRepository.createLog(
                            LogDraft(
                                level = LogLevel.INFO.description,
                                message = "Venda realizada / registada",
                                module = "Sells",
                                userId = products.order.userId,
                                metadataJson = stringSellMetadata
                            )
                        )
                    } catch (ex: IllegalStateException) {
                        call.respond(HttpStatusCode.BadRequest, "Illegal state")
                        val errorMetadata = ErrorMetadata(error = ex.message)
                        val stringErrorMetadata = Json.encodeToString(errorMetadata)
                        LogRepository.createLog(
                            LogDraft(
                                level = LogLevel.ERROR.description,
                                message = "Falha ao registar venda",
                                module = "Sells",
                                userId = products.order.userId,
                                metadataJson = stringErrorMetadata
                            )
                        )
                    } catch (ex: JsonConvertException) {
                        call.respond(HttpStatusCode.BadRequest, "json error")
                    }
                }
            }
        }

        // OrdersItems ------------->>
        authenticate("auth-jwt") {
            get("/orders_items") {
                val ordersItems = OrderItemRepository.getOrdersItems()
                call.respond(ordersItems)
            }
        }


        route("/system_configurations") {
            get("/get-all") {
                val sysConfigs = SysSettingsRepository.sysConfigtems()
                call.respond(sysConfigs)
            }

            get("/package-name") {
                val activePackageName = SysSettingsRepository.getPackageName()
                if (activePackageName != null) {
                    call.respond(HttpStatusCode.OK, activePackageName)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Campo não encontrado")
                }
            }

            authenticate("auth-jwt") {
                put("/edit-configurantions") {
                    try {
                        val sysConfig = call.receive<SysConfigItem>()
                        SysSettingsRepository.editSysConfig(sysConfig)
                        call.respond(HttpStatusCode.OK, "Configuraçao actualizada com sucesso.")
                    } catch (ex: IllegalStateException) {
                        call.respond(HttpStatusCode.BadRequest)
                    } catch (ex: JsonConvertException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
        }

    }
}
