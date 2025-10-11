package com.samuelsumbane.infrastructure.repositories
//
import com.samuelsumbane.ssptdesktop.modules.ErrorMetadata
import com.samuelsumbane.ssptdesktop.modules.LogDraft
import com.samuelsumbane.ssptdesktop.modules.LogLevel
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

suspend fun sendEmail(
    receiver: String,
    subject: String,
    content: String
): Result<Int> {
    val sender = "samuelsumbane143@gmail.com"
    val password = "bmau rnft qree cfuv"
    val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.gmail.com") // Altere conforme o provedor
        put("mail.smtp.port", "587")
    }

    val session = Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(sender, password)
        }
    })

    return try {
        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(sender))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver))
            this.subject = subject
            setText(content)
        }

        Transport.send(message)
        newSuspendedTransaction(Dispatchers.IO) {
            LogRepository.createLog(
                LogDraft(
                    level = LogLevel.INFO.description,
                    message = "Envio de email bem-sucedido.",
                    module = "",
                    userId = null,
                    metadataJson = null
                )
            )
        }
        Result.success(200)
    } catch (e: MessagingException) {
        newSuspendedTransaction(Dispatchers.IO) {
            val errorMetadata = ErrorMetadata(error = e.message)
            val stringErrorMetadata = Json.encodeToString(errorMetadata)
            LogRepository.createLog(
                LogDraft(
                    level = LogLevel.INFO.description,
                    message = "Envio de email falhado.",
                    module = "",
                    userId = null,
                    metadataJson = stringErrorMetadata
                )
            )
        }
        Result.failure(e)
    }
}
