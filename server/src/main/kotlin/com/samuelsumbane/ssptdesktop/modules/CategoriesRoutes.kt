package com.samuelsumbane.ssptdesktop.modules

import com.samuelsumbane.infrastructure.repositories.CategoryRepository
import com.samuelsumbane.ssptdesktop.CategoryItem
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.categoriesRoutes() {
    route("/categories") {
        authenticate("auth-jwt") {
            get("/all-categories") {
                val categories = CategoryRepository.getCategories()
                call.respond(HttpStatusCode.OK, categories)
//                call.respondWi
            }
        }

        authenticate("auth-jwt") {
            post("/create-category") {
                try {
                    val category = call.receive<CategoryItem>()
                    CategoryRepository.createCategory(category)
                    call.respond(HttpStatusCode.Created, "Categoria adicionada com sucesso.")
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        authenticate("auth-jwt") {
            put("/update-category") {
                try {
                    val category = call.receive<CategoryItem>()
                    CategoryRepository.updateCategory(category)
                    call.respond(HttpStatusCode.Created)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        delete("/delete-category/{id}") {
            val categoryId = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException()
            val requestCode = CategoryRepository.removeCategory(categoryId)
            when (requestCode) {
                406 -> call.respond(HttpStatusCode.NotAcceptable, "A categoria com dados não pode ser deletado.")
                404 -> call.respond(HttpStatusCode.NotFound, "Nenhuma categoria encontrada com ID fornecido.")
                200 -> call.respond(HttpStatusCode.OK, "Categoria removida com sucesso.")
            }
        }
    }
}