package com.samuelsumbane.repository


import com.samuelsumbane.infrastructure.repositories.LogRepository
import com.samuelsumbane.infrastructure.repositories.SysSettingsRepository
import com.samuelsumbane.infrastructure.repositories.sendEmail
import com.samuelsumbane.ssptdesktop.repositories.generateSixDigitsCode
import com.samuelsumbane.ssptdesktop.repositories.getCurrentTimestamp
import com.samuelsumbane.ssptdesktop.repositories.longTimeToString
import com.samuelsumbane.ssptdesktop.CustomHttpStatusCode
import com.samuelsumbane.ssptdesktop.PasswordResetCodes
import com.samuelsumbane.ssptdesktop.Users
import com.samuelsumbane.ssptdesktop.modules.ChangeRoleDC
import com.samuelsumbane.ssptdesktop.modules.ChangeStatusDC
import com.samuelsumbane.ssptdesktop.modules.EmailDc
import com.samuelsumbane.ssptdesktop.modules.LogDraft
import com.samuelsumbane.ssptdesktop.modules.LogLevel
import com.samuelsumbane.ssptdesktop.modules.PasswordDraft
import com.samuelsumbane.ssptdesktop.modules.UpdateUserPersonalData
import com.samuelsumbane.ssptdesktop.modules.UserItem
import com.samuelsumbane.ssptdesktop.modules.UserItemDraft

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
//import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.mindrot.jbcrypt.BCrypt
import kotlin.time.Duration.Companion.minutes


sealed class UserResult {
    object CanNotSave : UserResult()
}


object UsersRepository {

    suspend fun getUsers(): List<UserItem> {
        return dbQuery {
            Users.selectAll().map {
                UserItem(
                    it[Users.id],
                    it[Users.name],
                    it[Users.email],
                    it[Users.passwordHash],
                    it[Users.role],
                    userStatus(it[Users.status]),
                    longTimeToString(it[Users.lastLogin])

                )
            }
        }
    }

//    fun getUsersEmails(): List<String> {
//        return Users.slice(Users.email).selectAll().map { it[Users.email] }
//    }


    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }

    suspend fun verifyUser() {
        newSuspendedTransaction(Dispatchers.IO) {
            val existingUser = Users.selectAll().empty()
            if (existingUser) {
                Users.insert {
                    it[name] = "Administrador SS"
                    it[email] = "admin@gmain"
                    it[passwordHash] = BCrypt.hashpw("1111", BCrypt.gensalt())
                    it[role] = "Administrador"
                    it[status] = 1
                    it[lastLogin] = getCurrentTimestamp()
                }
            }
        }
    }

    suspend fun loginUser(email: String, password: String, timeZoneId: String): UserItem? {
        return newSuspendedTransaction(Dispatchers.IO) {
            Users.select { (Users.email eq email) }.firstOrNull()?.let {
                val passwordMatches = verifyPassword(password, it[Users.passwordHash])
                if (passwordMatches) {
                    Users.update ({ Users.email eq email }) {
                        it[lastLogin] = getCurrentTimestamp()
                    }

                    UserItem(
                        it[Users.id],
                        it[Users.name],
                        it[Users.email],
                        "",
                        it[Users.role],
                        userStatus(it[Users.status]),
                        longTimeToString(it[Users.lastLogin], timeZoneId),
                    )
                }  else null
            }
        }
    }


    suspend fun addUser(user: UserItemDraft): StatusAndMessage {
        return newSuspendedTransaction(Dispatchers.IO) {
            val sysPackage = SysSettingsRepository.getConfigString("active_package")
            if (sysPackage != null) {
                val usersCount = Users.selectAll().count().toInt()
                when (sysPackage) {
                    "Lite" -> {
                        if (usersCount < 3) {
                            val result= addUserDraft(user)
                            result.fold(
                                onSuccess = { StatusAndMessage(201, it.toString()) },
                                onFailure = { StatusAndMessage(203, "Erro ao criar usuário") }
                            )
                        } else {
                            StatusAndMessage(101, HttpResponseText.LITE_PACKAGE_REFUSED_USER.stringValue)
                        }
                    }

                    "Plus" -> {
                        if (usersCount < 10) {
                            val result= addUserDraft(user)
                            result.fold(
                                onSuccess = { StatusAndMessage(201, it.toString()) },
                                onFailure = { StatusAndMessage(203, "Erro ao criar usuário") }
                            )
                        } else {
                            StatusAndMessage(102, HttpResponseText.PLUS_PACKAGE_REFUSED_USER.stringValue)
                        }
                    }

                    "Pro" -> {
                        val result= addUserDraft(user)
                        result.fold(
                            onSuccess = { StatusAndMessage(201, it.toString()) },
                            onFailure = { StatusAndMessage(203, "Erro ao criar usuário") }
                        )
                    }

                    else -> StatusAndMessage(103, HttpResponseText.NOTFOUND_VALID_PACKAGE.stringValue)
                }
            } else StatusAndMessage(404, HttpResponseText.NOTFOUND_VALID_PACKAGE.stringValue)
        }
    }

    private suspend fun addUserDraft(user: UserItemDraft): Result<Int> {
        return try {
            val code = newSuspendedTransaction(Dispatchers.IO) {
                val initialPassword = generateSixDigitsCode()
                Users.insert {
                    it[name] = user.name
                    it[email] = user.email
                    it[passwordHash] = BCrypt.hashpw(initialPassword, BCrypt.gensalt())
                    it[role] = user.role
                    it[status] = 1
                    it[lastLogin] = getCurrentTimestamp()
                }
                initialPassword.toInt()
            }
            Result.success(code)
        } catch (e: ExposedSQLException) {
            if (e.message?.contains("unique constraint") == true || e.message?.contains("duplicate key") == true) {
                Result.failure(IllegalArgumentException("O email já está em uso."))
            } else {
                Result.failure(e)
            }
        }
    }


    suspend fun updateUserPersonalData(user: UpdateUserPersonalData): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            val existingUser = Users.select { Users.id eq user.userId }.singleOrNull()
            if (existingUser != null) {
                Users.update ({ Users.id eq user.userId }) {
                    it[name] = user.userName
                    it[email] = user.userEmail
                }
                200
            } else {
                404
            }
        }
    }

    suspend fun changeUserStatus(user: ChangeStatusDC): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            val existingUser = Users.select { Users.id eq user.userId }.singleOrNull()
            if (existingUser != null) {
                Users.update ({ Users.id eq user.userId }) {
                    it[status] = user.status
                }
                200
            } else {

                404
            }
        }
    }

    suspend fun changeUserRole(user: ChangeRoleDC): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            val existingUser = Users.select { Users.id eq user.userId }.singleOrNull()
            if (existingUser != null) {
                Users.update ({ Users.id eq user.userId }) {
                    it[role] = user.role
                }
                200
            } else {
                404
            }
        }
    }

    suspend fun usersStatus(): Pair<Int, Int> {
//        var activeUsers = 0
//        var totalUsers = 0
//        newSuspendedTransaction(Dispatchers.IO) {
//            activeUsers = Users.select { (status eq 1) }.count().toInt()
//            totalUsers = Users.selectAll().count().toInt()
//        }
//        return Pair(activeUsers, totalUsers)
        return Pair(2, 5)
    }

    suspend fun updateUserPassword(userPass: PasswordDraft): Pair<Int, String> {
        var code = 200
        var newPassword = ""
        newSuspendedTransaction(Dispatchers.IO) {
            val existingUser = getUserById(userPass.userId)
            if (existingUser != null){
                if (userPass.hashedPassword == existingUser.passwordHash) {
                    Users.update ({ Users.id eq userPass.userId }) {
                        it[passwordHash] = BCrypt.hashpw(userPass.newPassword, BCrypt.gensalt())
                    }
                    code = 200
                    newPassword = userPass.newPassword
                } else {
                    //Wrong password ---------->>
                    code = 202;
                }
            } else {
               code = 404
            }
        }
        return Pair(code, newPassword)
    }



    suspend fun getUserById(userId: Int): UserItem? {
        return newSuspendedTransaction(Dispatchers.IO) {
            val row = Users.select { Users.id eq userId }.firstOrNull()

            row?.let {
                UserItem(
                    it[Users.id],
                    it[Users.name],
                    it[Users.email],
                    it[Users.passwordHash],
                    it[Users.role],
                    userStatus(it[Users.status]),
                    longTimeToString(it[Users.lastLogin])
                )
            }
        }
    }

    suspend fun getUserByUserName(userName: String): UserItem? {
        return newSuspendedTransaction(Dispatchers.IO) {
            val row = Users.select { Users.email eq userName }.firstOrNull()
            row?.let {
                UserItem(
                    it[Users.id],
                    it[Users.name],
                    it[Users.email],
                    it[Users.passwordHash],
                    it[Users.role],
                    userStatus(it[Users.status]),
                    longTimeToString(it[Users.lastLogin])
                )
            }
        }
    }


    suspend fun removeUser(userId: Int): Boolean = newSuspendedTransaction(Dispatchers.IO) {
        val rowsDeleted = Users.deleteWhere { id eq userId }
        rowsDeleted == 1
    }

    suspend fun sendUserPasswordRecovery(data: EmailDc): Pair<Int, Int> {
        return newSuspendedTransaction(Dispatchers.IO) {
            val emailFound = Users.select { Users.email eq data.email }.firstOrNull()
            if (emailFound != null) {
                val generatedCode = generateSixDigitsCode()
                val now = getCurrentTimestamp()
                val newExpiresAt = now.plus(15.minutes.inWholeMilliseconds)

                val sendEmailResponse = sendEmail(
                    receiver = emailFound[Users.email],
                    subject = "Recuperar a senha",
                    content = "Codigo de recuperação de senha: ${generatedCode.toInt()}" +
                            "\n O codigo expira em 15 minutos.\n Não compartilhe com ninguém."
                )

                sendEmailResponse.fold(
                    onSuccess = {
                        val existing = PasswordResetCodes
                            .select { PasswordResetCodes.userId eq emailFound[Users.id] }
                            .firstOrNull()

                        if (existing != null) {
                            PasswordResetCodes.update({ PasswordResetCodes.userId eq emailFound[Users.id] }) {
                                it[code] = generatedCode
                                it[createdAt] = now
                                it[expiresAt] = newExpiresAt
                            }
                        } else {
                            PasswordResetCodes.insert {
                                it[PasswordResetCodes.userId] = emailFound[Users.id]
                                it[PasswordResetCodes.code] = generatedCode
                                it[PasswordResetCodes.createdAt] = now
                                it[PasswordResetCodes.expiresAt] = newExpiresAt
                            }
                        }


                        LogRepository.createLog(
                            LogDraft(
                                level = LogLevel.INFO.description,
                                message = "Codigo de recuperação de senha enviado com sucesso.",
                                module = "User",
                                userId = emailFound[Users.id],
                                metadataJson = null
                            )
                        )
                        Pair(200, emailFound[Users.id])
                    },
                    onFailure = {
                        Pair(500, CustomHttpStatusCode.EMAIL_NOT_SENT.value)
                    }
                )


            } else {
                Pair(404, -1)
            }
        }
    }

    suspend fun setNewPassword(data: PasswordDraft): Result<Int> {
        return newSuspendedTransaction(Dispatchers.IO) {
            val userFound = Users.select { Users.id eq data.userId }.firstOrNull()
            if (userFound != null) {
                Users.update ({ Users.id eq data.userId }) {
                    it[passwordHash] = BCrypt.hashpw(data.newPassword, BCrypt.gensalt())
                }
                Result.success(200)
            } else {
                Result.failure(Exception("User not found (404)"))
            }
        }
    }

//    suspend fun verifyResetCode(data: VerifyResetCodeDraft): Boolean {
//        return newSuspendedTransaction {
//            val record = PasswordResetCodes
//                .select { (PasswordResetCodes.userId eq data.userId) and (PasswordResetCodes.code eq data.inputCode) }
//                .firstOrNull()
//
//            if (record != null) {
//                val expiresAt = record[PasswordResetCodes.expiresAt]
////                LocalDateTime.now().isBefore(expiresAt)
//                getCurrentTimestamp() < expiresAt
//            } else {
//                false
//            }
//        }
//    }

//    suspend fun deleteExpiredResetCodes() {
//        newSuspendedTransaction {
//            PasswordResetCodes.deleteWhere { PasswordResetCodes.expiresAt lessEq LocalDateTime.now() }
//        }
//    }



    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

private fun userStatus(status: Int): String {
    return when(status) {
        1 -> "Activo"
        else -> "Bloqueado"
    }
}

data class StatusAndMessage(
    val statusKey: Int,
    val statusValue: String,
)

enum class HttpResponseText(val stringValue: String) {
    LITE_PACKAGE_REFUSED_USER("O pacote Lite só pode ter o máximo de 3 usuários"),
    PLUS_PACKAGE_REFUSED_USER("O pacote Plus só pode ter o máximo de 10 usuários"),
    NOTFOUND_VALID_PACKAGE("Nenhum pacote válido encontrado."),
    NOTFOUND_SYSTEM_PACKAGE("o pacote do seu sistema não foi encontrado."),
    USER_EMAIL_IN_USE("Email está em uso")
}