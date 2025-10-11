import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

//package com.samuelsumbane.ssptdesktop
//
//
//import com.auth0.jwt.JWT
//import com.auth0.jwt.algorithms.Algorithm
//import com.samuelsumbane.infrastructure.models.ChangeRoleDC
//import com.samuelsumbane.infrastructure.models.ChangeStatusDC
//import com.samuelsumbane.infrastructure.models.EmailDc
//import com.samuelsumbane.infrastructure.models.LogDraft
//import com.samuelsumbane.infrastructure.models.LogLevel
//import com.samuelsumbane.infrastructure.models.LoginRequest
//import com.samuelsumbane.infrastructure.models.NotificationDraft
//import com.samuelsumbane.infrastructure.models.PasswordDraft
//import com.samuelsumbane.infrastructure.models.UpdateUserPersonalData
//import com.samuelsumbane.infrastructure.models.UserItemDraft
//import com.samuelsumbane.infrastructure.models.UserLogMetadata
//import com.samuelsumbane.infrastructure.models.Users
//import com.samuelsumbane.infrastructure.models.VerifyPasswordDC
//import com.samuelsumbane.infrastructure.models.VerifyResetCodeDraft
//import com.samuelsumbane.infrastructure.modules.branchesRoutes
//import com.samuelsumbane.infrastructure.modules.finance.payableRoutes
//import com.samuelsumbane.infrastructure.modules.finance.receivableRoutes
//import com.samuelsumbane.infrastructure.modules.logsRoutes
//import com.samuelsumbane.infrastructure.modules.notificationsRoutes
//import com.samuelsumbane.infrastructure.modules.reportRoutes
//import com.samuelsumbane.infrastructure.modules.stockRoutes
//import com.samuelsumbane.infrastructure.repositories.LogRepository
//import com.samuelsumbane.infrastructure.repositories.NotificationRepository
//import com.samuelsumbane.infrastructure.repositories.sendEmail
//import com.samuelsumbane.infrastructure.repositories.tokkens.TokenStore
//import com.samuelsumbane.infrastructure.repository.getCurrentTimestamp
//import com.samuelsumbane.infrastructure.repository.hasInternet
//import com.samuelsumbane.infrastructure.repository.longTimeToString
//import com.samuelsumbane.infrastructure.repository.respondWithCache
//import com.samuelsumbane.infrastructure.routes.*
//import com.samuelsumbane.repository.HttpResponseText
//import com.samuelsumbane.repository.UsersRepository
//import io.ktor.http.*
//import io.ktor.serialization.*
//import io.ktor.server.application.*
//import io.ktor.server.auth.*
//import io.ktor.server.auth.jwt.*
//import io.ktor.server.request.*
//import io.ktor.server.response.*
//import io.ktor.server.routing.*
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.TimeoutCancellationException
//import kotlinx.coroutines.withTimeout
//import kotlinx.serialization.json.Json
//import org.jetbrains.exposed.sql.select
//import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
//import java.net.URLDecoder
//import java.util.*
//
//fun Application.configureRouting() {
//    val currentDateTime = longTimeToString(getCurrentTimestamp())
//
//    routing {
//        get("/") {
//            call.respondText("Hello Wold!")
//        }
//
//        post("/send-email") {
//            sendEmail(
//                receiver = "maxyounger32@gmail.com",
//                subject = "Teste de sistema",
//                content = "Este é um e-mail de teste enviado pelo Ktor localmente."
//            )
//
//        }
//
//        authenticate("auth-jwt") {
//            get("/users") {
//                val users = UsersRepository.getUsers()
//                call.respondWithCache(users, cacheSeconds = 3600)
//            }
//        }
//
//
//        route("/user") {
//            authenticate("auth-jwt") {
//                post("/create_user") {
//                    try {
//                        val user = call.receive<UserItemDraft>()
//                        val (status, message) = UsersRepository.addUser(user)
//                        when (status) {
//                            101, 102, 103 -> call.respond(HttpStatusCode.NotAcceptable, message)
//                            201 -> call.respond(HttpStatusCode.Created, message)
//                            404 -> call.respond(HttpStatusCode.NotFound, HttpResponseText.NOTFOUND_SYSTEM_PACKAGE.stringValue)
//                        }
//                    } catch (ex: IllegalStateException) {
//                        call.respond(HttpStatusCode.BadRequest)
//                    } catch (ex: JsonConvertException) {
//                        call.respond(HttpStatusCode.BadRequest)
//                    }
//                }
//            }
//
//            post("/change-status") {
//                try {
//                    val user = call.receive<ChangeStatusDC>()
//                    val status = UsersRepository.changeUserStatus(user)
//                    if (status == 200) {
//                        call.respond(HttpStatusCode.OK, "Estado do usuário editado com sucesso.")
//                    } else {
//                        call.respond(HttpStatusCode.NotFound, "Nenhum usuário encontrado com ID fornecido.")
//                    }
//                } catch (ex: IllegalStateException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                } catch (ex: JsonConvertException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                }
//            }
//
//            authenticate("auth-jwt") {
//                put("/update-user-personal-data") {
//                    try {
//                        val user = call.receive<UpdateUserPersonalData>()
//                        val status = UsersRepository.updateUserPersonalData(user)
//                        if (status == 200) {
//                            call.respond(HttpStatusCode.OK, "Estado do usuário editado com sucesso.")
//                        } else {
//                            call.respond(HttpStatusCode.NotFound, "Nenhum usuário encontrado com ID fornecido.")
//                        }
//                    } catch (ex: IllegalStateException) {
//                        call.respond(HttpStatusCode.BadRequest)
//                    } catch (ex: JsonConvertException) {
//                        call.respond(HttpStatusCode.BadRequest)
//                    }
//                }
//            }
//
////            authenticate("auth-jwt") {
//
////            }
//
//
//            authenticate("auth-jwt") {
//                post("/change-role") {
//                    try {
//                        val user = call.receive<ChangeRoleDC>()
//                        val status = UsersRepository.changeUserRole(user)
//                        if (status == 200) {
//                            call.respond(HttpStatusCode.OK, "Novo papel foi atribuido com sucesso.")
//                        } else {
//                            call.respond(HttpStatusCode.NotFound, "Nenhum usuário encontrado com ID fornecido.")
//                        }
//                    } catch (ex: IllegalStateException) {
//                        call.respond(HttpStatusCode.BadRequest)
//                    } catch (ex: JsonConvertException) {
//                        call.respond(HttpStatusCode.BadRequest)
//                    }
//                }
//            }
//
//            get("/usersStatus") {
//                val userStatus = UsersRepository.usersStatus()
//                call.respond(userStatus)
//            }
//
//            authenticate("auth-jwt") {
//                post("/verify-password") {
//                    try {
//                        val vp = call.receive<VerifyPasswordDC>()
//                        val passwordMatchs = UsersRepository.verifyPassword(vp.actualPassword, vp.hashedPassword)
//                        if (passwordMatchs) {
//                            call.respond(HttpStatusCode.Accepted, "A senha está correcta")
//                        } else {
//                            call.respond(HttpStatusCode.NotAcceptable, "A senha está errada")
//                        }
//                    } catch (ex: IllegalStateException) {
//                        call.respond(HttpStatusCode.BadRequest)
//                    } catch (ex: JsonConvertException) {
//                        call.respond(HttpStatusCode.BadRequest)
//                    }
//                }
//            }
//
//
//            get("by-user-id/{id}") {
//                val userId = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException()
//                val userData = UsersRepository.getUserById(userId)
//                if (userData != null) {
//                    call.respond(userData)
//                } else {
//                    call.respond(HttpStatusCode.NotFound, "Nenhum usuário encontrado com ID fornecido.")
//                }
//            }
//
//            post("/login") {
//                val credentials = call.receive<LoginRequest>()
//                val timeZoneId = call.request.queryParameters["timezoneid"]
//                if (timeZoneId == null) {
//                    call.respond(HttpStatusCode.NotFound, "Timezoneid not found")
//                    return@post
//                }
//                val decodedTimeZoneId = URLDecoder.decode(timeZoneId, "UTF-8")
//
//                val userData = UsersRepository.loginUser(credentials.email, credentials.password, decodedTimeZoneId)
//                if (userData != null) {
//                    val tokenId = UUID.randomUUID().toString()
//                    val token = JWT.create()
//                        .withIssuer(ISSUER)
//                        .withAudience(AUDIENCE)
//                        .withClaim("username", credentials.email)
//                        .withClaim("tokenId", tokenId) // salvamos o ID do token
//                        .sign(Algorithm.HMAC256(SECRET))
//                    TokenStore.addToken(tokenId)
//
//                    call.respond(HttpStatusCode.Accepted, mapOf("token" to token, "userRole" to userData.role))
//
//                    val userLogMetadata = UserLogMetadata(
//                        ip = null,
//                    )
//
//                    val stringUserLogMetadata = Json.encodeToString(userLogMetadata)
//
//                    LogRepository.createLog(
//                        LogDraft(
//                            level = LogLevel.INFO.description,
//                            message = "Login bem-sucedido",
//                            module = "Users",
//                            userId = userData.id,
//                            metadataJson = stringUserLogMetadata
//                        )
//                    )
//                } else {
//                    newSuspendedTransaction(Dispatchers.IO) {
//                        val actualDateTime = longTimeToString(getCurrentTimestamp())
//                        val checkEmailRow = Users.select { Users.email eq credentials.email }.singleOrNull()
//                        val notfTitle = "Login não aceite"
//                        NotificationRepository.createNotification(
//                            if (checkEmailRow == null) {
//                                NotificationDraft(
//                                    null,
//                                    notfTitle,
//                                    "Tentativa de login com email desconhecido: ${credentials.email}, $actualDateTime",
//                                    2,
//                                    autoDeleteAfter = null
//                                )
//                            } else {
//                                val userId = checkEmailRow[Users.id]
//                                NotificationDraft(
//                                    userId,
//                                    notfTitle,
//                                    "Foi registada uma tentativa de acesso falhada à sua conta em $actualDateTime. Se não foi você, considere alterar a palavra-passe.",
//                                    2,
//                                    autoDeleteAfter = null
//                                )
//                            }
//                        )
//                        LogRepository.createLog(
//                            LogDraft(
//                                level = LogLevel.WARN.description,
//                                message = "Tentativa de login falhada",
//                                module = "Users",
//                                userId = null,
//                                metadataJson = ""
//                            )
//                        )
//                    }
//
//
//                    call.respond(HttpStatusCode.Unauthorized, "Usuário ou senha inválidos")
//                }
//            }
//
//            authenticate("auth-jwt") {
//                post("/logout") {
//                    val principal = call.principal<JWTPrincipal>()
//                    val tokenId = principal?.getClaim("tokenId", String::class)
//
//                    if (tokenId != null && TokenStore.isTokenActive(tokenId)) {
//                        TokenStore.removeToken(tokenId)
//                        call.respond(HttpStatusCode.OK, "Logout efetuado com sucesso.")
//                    } else {
//                        call.respond(HttpStatusCode.Unauthorized, "Token inválido ou já expirado.")
//                    }
//                }
//            }
//
//
//            authenticate("auth-jwt") {
//                get("/check-session") {
//                    withTimeout(5000) {
//                        try {
//                            val principal = call.principal<JWTPrincipal>()
//                            val username = principal?.getClaim("username", String::class)
//                            val tokenId = principal?.getClaim("tokenId", String::class)
//
//                            if (username != null && tokenId != null && TokenStore.isTokenActive(tokenId)) {
//                                val userData = UsersRepository.getUserByUserName(username)
//                                call.respond(HttpStatusCode.Accepted, mapOf(
//                                    "message" to "Usuário autenticado",
//                                    "userid" to userData?.id.toString(),
//                                    "username" to userData?.name.toString(),
//                                    "userrole" to userData?.role.toString()
//                                ))
//                            } else {
//                                call.respond(HttpStatusCode.NotFound, "Token inválido ou sessão expirada")
//                            }
//                        } catch (e: TimeoutCancellationException) {
//                            call.respond(HttpStatusCode.RequestTimeout, "Sessão demorou demais")
//                        }
//                    }
//
//
//                }
//            }
////            updateUserPassword
//            authenticate("auth-jwt") {
//                put("/update-user-password") {
//                    try {
//                        val user = call.receive<PasswordDraft>()
//                        val (status, newPassword) = UsersRepository.updateUserPassword(user)
//                        when (status) {
//                            200 -> call.respond(HttpStatusCode.OK, "Senha alterada com sucesso. Sua nova senha é $newPassword")
//                            202 -> call.respond(HttpStatusCode.NotAcceptable, "Senha não alterada, a senha fornecida não corresponde a actual.")
//                            404 -> call.respond(HttpStatusCode.NotFound, "Nenhum usuário encontrado com ID fornecido.")
//                        }
//
//                        LogRepository.createLog(
//                            LogDraft(
//                                level = LogLevel.INFO.description,
//                                message = "Alteração de senha",
//                                module = "Users",
//                                userId = user.userId,
//                                metadataJson = null
//                            )
//                        )
//                    } catch (ex: IllegalStateException) {
//                        call.respond(HttpStatusCode.BadRequest)
//                    } catch (ex: JsonConvertException) {
//                        call.respond(HttpStatusCode.BadRequest)
//                    }
//                }
//            }
//
//
//            post("/verify-recover-password-code") {
//                val data = call.receive<VerifyResetCodeDraft>()
//
//                if (!hasInternet()) {
//                    call.respond(HttpStatusCode.ServiceUnavailable, "Offine")
//                    return@post
//                }
//
//                val userData = UsersRepository.verifyResetCode(data)
//                if (userData) {
//                    call.respond(HttpStatusCode.OK, "O codigo foi verificado com sucesso.")
//                } else {
//                    call.respond(HttpStatusCode.NotAcceptable, "O codigo já expirou.")
//                }
//            }
//
//            put("/set-new-password") {
//                try {
//                    val passwordData = call.receive<PasswordDraft>()
//                    val statusStatus = UsersRepository.setNewPassword(passwordData)
//                    if (statusStatus.isSuccess) {
//                        call.respond(HttpStatusCode.OK, "Sua nova senha é: ${passwordData.newPassword}")
//                    } else {
//                        call.respond(HttpStatusCode.NotFound, "Nenhum usuário encontrado com id fornecido")
//                    }
//                } catch (ex: IllegalStateException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                } catch (ex: JsonConvertException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                }
//            }
//
//            post("/verify-user-email") {
//                try {
//                    val userEmail = call.receive<EmailDc>()
//
//                    if (!hasInternet()) {
//                        call.respond(HttpStatusCode.ServiceUnavailable, "Servidor sem conexão com a internet.")
//                        return@post
//                    }
//
//                    val (requestStatus, requestValue) = UsersRepository.sendUserPasswordRecovery(userEmail)
//
//                    when (requestStatus) {
//                        200 ->  call.respond(HttpStatusCode.OK, "$requestValue - O codigo de recuperação de senha foi enviado para seu email.")
//                        500 -> call.respond(HttpStatusCode.BadGateway, "Houve um erro ao enviar o codigo de recuperação. Certifique-se de estar conectado a internet e usar o email valido."
//                        )
//                        else -> call.respond(HttpStatusCode.NotAcceptable,"Email é inválido.")
//                    }
//                } catch (ex: IllegalStateException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                } catch (ex: JsonConvertException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                }
//            }
//
//        }
//
//        route("{...}") {
//            handle {
//                call.respondText(
//                    "Erro 404 - Página não encontrada!",
//                    status = HttpStatusCode.NotFound
//                )
//            }
//        }
//
//        if (Config.hasModule("suppliers")) suppliersRoutes()
//        if (Config.hasModule("sales")) reportRoutes()
//        if (Config.hasModule("products")) productsRoutes()
//        if (Config.hasModule("clients")) clientsRoutes()
//        if (Config.hasModule("categories")) categoriesRoutes()
//        if (Config.hasModule("stock")) stockRoutes()
//        if (Config.hasModule("branches")) branchesRoutes()
//        if (Config.hasModule("payables")) payableRoutes()
//        if (Config.hasModule("receivables")) receivableRoutes()
//        if (Config.hasModule("owners")) ownersRoutes()
//        if (Config.hasModule("notifications")) notificationsRoutes()
//        if (Config.hasModule("logs")) logsRoutes()
////        if (Config.hasModule("")) Routes()
//    }
//}
//
//
//object Config {
//    // Lite ------>>
////    private val enabledModules = setOf("sales", "stock")
//
//    // Premium ------->>
//    private val enabledModules = setOf(
//        "sales",
//        "stock",
//        "categories",
//        "clients",
//        "products",
//        "suppliers",
//        "branches",
//        "payables",
//        "receivables",
//        "owners",
//        "notifications",
//        "logs"
//    )
//
//    fun hasModule(module: String): Boolean {
//        return enabledModules.contains(module)
//    }
//}

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello Wold!")
        }
    }
}